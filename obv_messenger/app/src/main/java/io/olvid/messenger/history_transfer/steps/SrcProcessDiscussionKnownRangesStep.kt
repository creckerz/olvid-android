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
import io.olvid.messenger.history_transfer.json.DstDiscussionExpectedRanges
import io.olvid.messenger.history_transfer.types.SrcTransferProtocolState
import kotlinx.coroutines.Runnable


class SrcProcessDiscussionKnownRangesStep(
    val srcTransferProtocolState: SrcTransferProtocolState,
    val dstDiscussionExpectedRanges: DstDiscussionExpectedRanges,
) : Runnable {

    override fun run() {
        Logger.i("🫠 Running step SrcProcessDiscussionKnownRangesStep")
        // this step should normally only be run after SrcSendDiscussionsStep has run its first part
        val discussionIdentifiers = srcTransferProtocolState.discussionIdentifiers ?: return

        val discussionIdentifier = dstDiscussionExpectedRanges.discussion ?: return
        val expectedRanges = dstDiscussionExpectedRanges.getRangesByThreadAndSender() ?: emptyMap()

        if (discussionIdentifiers.contains(discussionIdentifier).not()
            || srcTransferProtocolState.expectedDiscussionRanges.contains(discussionIdentifier)) {
            // only accept discussionIdentifier for which we have sent a range and not already received one
            return
        }

        srcTransferProtocolState.expectedDiscussionRanges[discussionIdentifier] = expectedRanges
        srcTransferProtocolState.totalMessageCount += countMessagesInRanges(rangesByThreadAndSender = expectedRanges)
    }
}