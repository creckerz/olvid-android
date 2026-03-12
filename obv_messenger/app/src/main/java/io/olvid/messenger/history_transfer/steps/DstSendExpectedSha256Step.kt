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
import io.olvid.messenger.databases.AppDatabase
import io.olvid.messenger.history_transfer.json.DstExpectedSha256
import io.olvid.messenger.history_transfer.json.SrcDiscussionList
import io.olvid.messenger.history_transfer.types.DstTransferProtocolState
import io.olvid.messenger.history_transfer.types.TransferMessageType
import io.olvid.messenger.history_transfer.types.TransferTransportDelegate
import kotlinx.coroutines.Runnable


class DstSendExpectedSha256Step(
    val dstTransferProtocolState: DstTransferProtocolState,
    val srcDiscussionList: SrcDiscussionList,
    val transferTransportDelegate: TransferTransportDelegate
) : Runnable {

    override fun run() {
        Logger.i("🫠 Running step DstSendExpectedSha256Step")
        val db = AppDatabase.getInstance()

        // this step should normally only be run on an empty state
        if (dstTransferProtocolState.srcDiscussionIdentifiers != null || dstTransferProtocolState.expectedSha256s != null) {
            return
        }

        val srcSha256s = srcDiscussionList.sha256s ?: emptyMap()
        dstTransferProtocolState.srcDiscussionIdentifiers = srcDiscussionList.discussions?.toSet() ?: emptySet()

        val knownSha256s = srcSha256s.keys.chunked(100).flatMap { batch ->
            db.fyleDao().filterKnownAndComplete(batch.map { obvBytesKey -> obvBytesKey.bytes })
        }



        val expectedSha256 = srcSha256s.toMutableMap()
        knownSha256s.forEach {
            expectedSha256.remove(ObvBytesKey(it))
        }

        dstTransferProtocolState.expectedSha256s = expectedSha256
        dstTransferProtocolState.totalBytes = expectedSha256.values.sum()

        transferTransportDelegate.sendJsonMessage(
            messageType = TransferMessageType.DST_EXPECTED_SHA256,
            serializedMessage = transferTransportDelegate.objectMapper.writeValueAsBytes(
                DstExpectedSha256().apply {
                    this.expectedSha256s = expectedSha256
                }
            )
        )
    }
}