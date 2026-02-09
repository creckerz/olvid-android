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

enum class TransferMessageType(val value: Byte) {
    ACK(0),
    SRC_DISCUSSION_LIST(1),
    SRC_DISCUSSION_RANGES(2),
    DST_EXPECTED_SHA256(3),
    DST_DISCUSSION_EXPECTED_RANGES(4),
    SRC_MESSAGES(5),
    DST_REQUEST_SHA256(6),
    DST_DO_NOT_REQUEST_SHA256(7),
    SRC_SHA256(8),
    SRC_DISCUSSION_DONE(9),
    SRC_TRANSFER_DONE(10);


    companion object {
        fun of(value: Byte): TransferMessageType? {
            return when(value) {
                0.toByte() -> ACK
                1.toByte() -> SRC_DISCUSSION_LIST
                2.toByte() -> SRC_DISCUSSION_RANGES
                3.toByte() -> DST_EXPECTED_SHA256
                4.toByte() -> DST_DISCUSSION_EXPECTED_RANGES
                5.toByte() -> SRC_MESSAGES
                6.toByte() -> DST_REQUEST_SHA256
                7.toByte() -> DST_DO_NOT_REQUEST_SHA256
                8.toByte() -> SRC_SHA256
                9.toByte() -> SRC_DISCUSSION_DONE
                10.toByte() -> SRC_TRANSFER_DONE
                else -> null
            }
        }
    }
}