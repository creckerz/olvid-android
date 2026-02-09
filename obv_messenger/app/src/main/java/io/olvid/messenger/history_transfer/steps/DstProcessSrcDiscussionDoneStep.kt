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
import io.olvid.messenger.history_transfer.json.SrcDiscussionDone
import io.olvid.messenger.history_transfer.types.DstTransferProtocolState
import io.olvid.messenger.history_transfer.types.TransferTransportDelegate


class DstProcessSrcDiscussionDoneStep(
    val dstTransferProtocolState: DstTransferProtocolState,
    val srcDiscussionDone: SrcDiscussionDone,
    val transferTransportDelegate: TransferTransportDelegate,
) : Runnable  {
    override fun run() {
        Logger.i("🫠 Running step DstProcessSrcDiscussionDoneStep")

        // this step should normally only be run after receiving message ranges
        if (dstTransferProtocolState.expectedDiscussionRanges.isEmpty() || dstTransferProtocolState.expectedSha256s == null) {
            return
        }

        // srcMessages must be complete
        val discussionIdentifier = srcDiscussionDone.discussion ?: return
        val missingMessageCount = srcDiscussionDone.missingMessageCount ?: return

        if (dstTransferProtocolState.expectedDiscussionRanges.containsKey(discussionIdentifier).not()) {
            Logger.i("🫠 DstProcessSrcDiscussionDoneStep: received discussion done for unexpected discussion")
            return
        }

        dstTransferProtocolState.receivedMessageCount += missingMessageCount
        if (dstTransferProtocolState.updateProgress()) {
            DstRequestMissingSha256Step(dstTransferProtocolState, transferTransportDelegate).run()
        }
    }
}