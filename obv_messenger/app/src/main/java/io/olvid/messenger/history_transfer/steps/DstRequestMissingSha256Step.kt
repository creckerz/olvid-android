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

package io.olvid.messenger.history_transfer.steps

import io.olvid.engine.Logger
import io.olvid.messenger.databases.AppDatabase
import io.olvid.messenger.history_transfer.json.DstDoNotRequestSha256
import io.olvid.messenger.history_transfer.json.DstRequestSha256
import io.olvid.messenger.history_transfer.types.DstTransferProtocolState
import io.olvid.messenger.history_transfer.types.TransferMessageType
import io.olvid.messenger.history_transfer.types.TransferTransportDelegate
import kotlinx.coroutines.Runnable


class DstRequestMissingSha256Step(
    val dstTransferProtocolState: DstTransferProtocolState,
    val transferTransportDelegate: TransferTransportDelegate,
) : Runnable {
    override fun run() {
        // make sure we never run this step twice
        if (dstTransferProtocolState.missingSha256WereRequested) {
            return
        }
        dstTransferProtocolState.missingSha256WereRequested = true

        Logger.i("🫠 Running step DstRequestMissingSha256Step")

        val db = AppDatabase.getInstance()

        // check all expectedSha256s and see if some of them were never requested
        dstTransferProtocolState.expectedSha256s?.forEach { (sha256Key, size) ->
            if (dstTransferProtocolState.requestedSha256.contains(sha256Key).not()) {
                dstTransferProtocolState.requestedSha256.add(sha256Key)
                // for unrequested sha256, check if a fyle exists to received them of not
                db.fyleDao().getBySha256(sha256Key.bytes)?.also {
                    transferTransportDelegate.sendJsonMessage(
                        messageType = TransferMessageType.DST_REQUEST_SHA256,
                        serializedMessage = transferTransportDelegate.objectMapper.writeValueAsBytes(
                            DstRequestSha256().apply {
                                this.sha256 = sha256Key.bytes
                            }
                        )
                    )
                } ?: run {
                    transferTransportDelegate.sendJsonMessage(
                        messageType = TransferMessageType.DST_DO_NOT_REQUEST_SHA256,
                        serializedMessage = transferTransportDelegate.objectMapper.writeValueAsBytes(
                            DstDoNotRequestSha256().apply {
                                this.sha256 = sha256Key.bytes
                            }
                        )
                    )
                    // account for the bytes that will never be received
                    dstTransferProtocolState.receivedBytes += size
                    dstTransferProtocolState.updateProgress()
                }
            }
        }
    }
}