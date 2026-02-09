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

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import io.olvid.engine.datatypes.EtaEstimator


abstract class TransferProtocolState {
    val transferProgress: MutableState<TransferProgress> = mutableStateOf(TransferProgress.ContactingOtherDevice)

    // used to estimate ETA of operation, they are created once the total messages/bytes are known
    protected var messagesEtaEstimator: EtaEstimator? = null
    protected var filesEtaEstimator: EtaEstimator? = null
    val messagesSpeedAndEta: MutableState<EtaEstimator.SpeedAndEta?> = mutableStateOf(null)
    val filesSpeedAndEta: MutableState<EtaEstimator.SpeedAndEta?> = mutableStateOf(null)

    abstract fun updateProgress(): Boolean
}