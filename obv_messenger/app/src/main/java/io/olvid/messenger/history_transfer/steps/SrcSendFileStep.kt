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

package io.olvid.messenger.history_transfer.steps

import io.olvid.engine.Logger
import io.olvid.engine.engine.types.ObvBytesKey
import io.olvid.messenger.App
import io.olvid.messenger.databases.AppDatabase
import io.olvid.messenger.history_transfer.json.DstRequestSha256
import io.olvid.messenger.history_transfer.json.SrcTransferDone
import io.olvid.messenger.history_transfer.types.AttachmentProgressListener
import io.olvid.messenger.history_transfer.types.SrcTransferProtocolState
import io.olvid.messenger.history_transfer.types.TransferMessageType
import io.olvid.messenger.history_transfer.types.TransferTransportDelegate
import kotlinx.coroutines.Runnable
import java.io.ByteArrayInputStream
import java.io.FileInputStream
import java.util.concurrent.Executor


class SrcSendFileStep(
    val srcTransferProtocolState: SrcTransferProtocolState,
    val dstRequestSha256: DstRequestSha256,
    val transferTransportDelegate: TransferTransportDelegate,
    val executor: Executor,
) : Runnable {

    override fun run() {
        Logger.i("🫠 Running step SrcSendFileStep")
        val db = AppDatabase.getInstance()

        val sha256 = dstRequestSha256.sha256 ?: return
        val sha256key = ObvBytesKey(sha256)
        // only send sha256 that were planned
        val size = srcTransferProtocolState.expectedSha256s?.get(sha256key) ?: return

        if (srcTransferProtocolState.sentSha256.add(sha256key).not()) {
            // if already sent, do nothing
            return
        }

        // offload to another thread --> we do not want to block the service's executor
        transferTransportDelegate.queueSendAttachmentTask {
            var totalSent = 0L
            var fileNotFound = true
            db.fyleDao().getBySha256(sha256)?.filePath?.let { filePath ->
                try {
                    FileInputStream(App.absolutePathFromRelative(filePath)).use { fileInputStream ->
                        fileNotFound = false
                        transferTransportDelegate.sendAttachment(
                            sha256 = sha256,
                            size = size,
                            inputStream = fileInputStream,
                            attachmentProgressListener = object : AttachmentProgressListener {
                                override fun bytesTransferred(count: Long) {
                                    val safeCount = count.coerceAtMost(size - totalSent)
                                    totalSent += safeCount
                                    executor.execute {
                                        srcTransferProtocolState.sentBytes += safeCount
                                        if (srcTransferProtocolState.updateProgress()) {
                                            executor.execute {
                                                transferTransportDelegate.sendJsonMessage(
                                                    messageType = TransferMessageType.SRC_TRANSFER_DONE,
                                                    serializedMessage = transferTransportDelegate.objectMapper.writeValueAsBytes(
                                                        SrcTransferDone()
                                                    ),
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        )
                    }
                } catch (e: Exception) {
                    Logger.x(e)
                }
            }
            if (fileNotFound) {
                // if we could not open the file, send an empty file (at destination, sha256 check will fail)
                transferTransportDelegate.sendAttachment(
                    sha256 = sha256,
                    size = 0,
                    inputStream = ByteArrayInputStream(ByteArray(0)),
                )
            }

            executor.execute {
                if (transferTransportDelegate.isAborted().not()) {
                    // in case the transfer was not complete (or the file was not found), we still count the missing bytes
                    srcTransferProtocolState.sentBytes += size - totalSent
                    if (srcTransferProtocolState.updateProgress()) {
                        transferTransportDelegate.sendJsonMessage(
                            messageType = TransferMessageType.SRC_TRANSFER_DONE,
                            serializedMessage = transferTransportDelegate.objectMapper.writeValueAsBytes(
                                SrcTransferDone()
                            ),
                        )
                    }
                }
            }
        }
    }
}