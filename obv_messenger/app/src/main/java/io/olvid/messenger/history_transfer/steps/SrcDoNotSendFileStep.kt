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
import io.olvid.engine.engine.types.ObvBytesKey
import io.olvid.messenger.history_transfer.json.DstDoNotRequestSha256
import io.olvid.messenger.history_transfer.json.SrcTransferDone
import io.olvid.messenger.history_transfer.types.SrcTransferProtocolState
import io.olvid.messenger.history_transfer.types.TransferMessageType
import io.olvid.messenger.history_transfer.types.TransferTransportDelegate
import kotlinx.coroutines.Runnable


class SrcDoNotSendFileStep(
    val srcTransferProtocolState: SrcTransferProtocolState,
    val dstDoNotRequestSha256: DstDoNotRequestSha256,
    val transferTransportDelegate: TransferTransportDelegate,
) : Runnable {

    override fun run() {
        Logger.i("🫠 Running step SrcDoNotSendFileStep")

        val sha256 = dstDoNotRequestSha256.sha256 ?: return
        val sha256key = ObvBytesKey(sha256)
        // only send sha256 that were planned
        val size = srcTransferProtocolState.expectedSha256s?.get(sha256key) ?: return

        if (srcTransferProtocolState.sentSha256.add(sha256key).not()) {
            // if already sent, do nothing
            return
        }

        // count the missing bytes
        srcTransferProtocolState.sentBytes += size
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