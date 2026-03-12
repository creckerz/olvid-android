/*
 *  Olvid for Android
 *  Copyright © 2019-2026 Olvid SAS
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

package io.olvid.messenger.billing

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.olvid.messenger.App
import io.olvid.messenger.notifications.AndroidNotificationManager
import java.util.concurrent.TimeUnit

class SubscriptionReminderWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    companion object {
        private const val WORK_TAG = "subscription_reminder"

        fun scheduleNotification(delayMs: Long) {
            val workManager = WorkManager.getInstance(App.getContext())
            workManager.cancelAllWorkByTag(WORK_TAG)

            val request = OneTimeWorkRequestBuilder<SubscriptionReminderWorker>()
                .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
                .addTag(WORK_TAG)
                .build()

            workManager.enqueue(request)
        }
    }

    override fun doWork(): Result {
        AndroidNotificationManager.displaySubscriptionReminderNotification()
        return Result.success()
    }
}
