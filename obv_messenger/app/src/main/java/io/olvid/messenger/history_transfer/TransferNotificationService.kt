/*
 *  Olvid for Android
 *  Copyright © 2019-2025 Olvid SAS
 *
 *  This file is part of Olvid for Android.
 *
 *  Olvid is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License, version 3,
 *  as published by the Free Software Foundation.
 *
 *  Olvid is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with Olvid.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.olvid.messenger.history_transfer

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.drawable.Icon
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import io.olvid.engine.Logger
import io.olvid.messenger.R
import io.olvid.messenger.history_transfer.types.TransferProgress
import io.olvid.messenger.notifications.AndroidNotificationManager
import java.util.Timer
import kotlin.concurrent.timer


class TransferNotificationService : Service() {
    lateinit var timer: Timer
    var notificationBuilder: NotificationCompat.Builder? = null

    override fun onCreate() {
        // start a time to monitor the progress of the transfer
        timer = timer(
            name = "TransferNotificationService-timer",
            period = 500,
            initialDelay = 500,
        ) {
            updateNotification()
        }
    }

    override fun onDestroy() {
        timer.cancel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when(intent?.action) {
            ACTION_START -> showNotification()
            ACTION_ABORT -> TransferService.abortOngoingTransfer()
        }
        return START_NOT_STICKY
    }

    private fun showNotification() {
        try {
            getNotification()?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    startForeground(SERVICE_ID, it, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
                } else {
                    startForeground(SERVICE_ID, it)
                }
            } ?: run {
                stopSelf()
            }
        } catch (e: Exception) {
            Logger.x(e)
        }
    }

    private fun updateNotification() {
        getNotification()?.also {
            val notificationManager = NotificationManagerCompat.from(this)
            try {
                notificationManager.notify(SERVICE_ID, it)
            } catch (_ : SecurityException) {}
        } ?: run {
            stopSelf()
        }
    }

    private fun getNotification() : Notification? {
        val progress = TransferService.getTransferProgress()?.value ?: return null
        if (notificationBuilder == null) {
            notificationBuilder = NotificationCompat.Builder(
                this,
                AndroidNotificationManager.TRANSFER_SERVICE_NOTIFICATION_CHANNEL_ID
            ).apply {
                val abortPendingIntent = PendingIntent.getService(
                    this@TransferNotificationService,
                    0,
                    Intent(this@TransferNotificationService, TransferNotificationService::class.java).apply {
                        action = ACTION_ABORT
                    },
                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                val openActivityPendingIntent = PendingIntent.getActivity(
                    this@TransferNotificationService,
                    0,
                    Intent(this@TransferNotificationService, HistoryTransferActivity::class.java).apply {
                        this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    },
                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                setSmallIcon(R.drawable.ic_o)
                setLargeIcon(Icon.createWithResource(this@TransferNotificationService, R.drawable.ic_transfer_white))
                setOngoing(true)
                setSilent(true)
                setPriority(NotificationCompat.PRIORITY_LOW)
                setGroup("silent")
                setGroupSummary(false)
                setShowWhen(false)
                setColorized(true)
                setColor(ContextCompat.getColor(this@TransferNotificationService, R.color.olvid_gradient_light))
                setContentTitle(getString(R.string.history_transfer_title))
                setContentIntent(openActivityPendingIntent)
                addAction(R.drawable.ic_close, getString(R.string.button_label_abort), abortPendingIntent)
            }
        }

        return notificationBuilder?.apply {
            when (progress) {
                TransferProgress.ContactingOtherDevice -> {
                    setStyle(null)
                    setContentText(getString(R.string.history_transfer_step_contacting_other_device))
                }
                TransferProgress.Connecting -> {
                    setStyle(null)
                    setContentText(getString(R.string.history_transfer_step_connecting))
                }
                TransferProgress.Negotiating -> {
                    setStyle(null)
                    setContentText(getString(R.string.history_transfer_step_negotiating))
                }
                is TransferProgress.TransferringMessages -> {
                    setStyle(
                        NotificationCompat.ProgressStyle()
                            .setProgress(progress.progress)
                            .addProgressSegment(
                                NotificationCompat.ProgressStyle.Segment(progress.total)
                                    .setColor(ContextCompat.getColor(this@TransferNotificationService, R.color.olvid_gradient_light))
                            )
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        setContentText(getString(R.string.history_transfer_step_transferring_messages))
                    } else {
                        setSubText(getString(R.string.history_transfer_title))
                        setContentText(null)
                        setContentTitle(getString(R.string.history_transfer_step_transferring_messages))
                    }
                }
                is TransferProgress.TransferringFiles ->  {
                    setStyle(
                        NotificationCompat.ProgressStyle()
                            .setProgress(((progress.progress * 1000) / progress.total).toInt())
                            .addProgressSegment(
                                NotificationCompat.ProgressStyle.Segment(1000)
                                    .setColor(ContextCompat.getColor(this@TransferNotificationService, R.color.olvid_gradient_light))
                            )
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        setContentText(getString(R.string.history_transfer_step_transferring_files))
                    } else {
                        setSubText(getString(R.string.history_transfer_title))
                        setContentText(null)
                        setContentTitle(getString(R.string.history_transfer_step_transferring_files))
                    }
                }
                TransferProgress.Finished -> {
                    setSubText(null)
                    setContentTitle(getString(R.string.history_transfer_title))
                    setStyle(null)
                    setContentText(getString(R.string.history_transfer_step_finished))
                }
                is TransferProgress.Failed -> {
                    setSubText(null)
                    setContentTitle(getString(R.string.history_transfer_title))
                    setStyle(null)
                    setContentText(getString(R.string.history_transfer_step_failed))
                }
            }
        }?.build()
    }

    // not a bound service
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val ACTION_START = "start"
        const val ACTION_ABORT = "abort"
        const val SERVICE_ID = 9002
    }
}