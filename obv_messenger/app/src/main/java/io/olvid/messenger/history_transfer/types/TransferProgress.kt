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

package io.olvid.messenger.history_transfer.types


sealed interface TransferProgress {
    data object ContactingOtherDevice: TransferProgress
    data object Connecting: TransferProgress
    data object Negotiating : TransferProgress
    data class TransferringMessages(val progress: Int, val total: Int) : TransferProgress
    data class TransferringFiles(val progress: Long, val total: Long) : TransferProgress
    data object Finished: TransferProgress
    data class Failed(val reason: TransferFailReason): TransferProgress
}
