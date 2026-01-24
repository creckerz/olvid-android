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

package io.olvid.messenger.billing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SubscriptionOfferViewModel : ViewModel() {

    var bytesOwnedIdentity: ByteArray? by mutableStateOf(null)
        private set

    fun updateBytesOwnedIdentity(bytesOwnedIdentity: ByteArray?) {
        if (this.bytesOwnedIdentity?.contentEquals(bytesOwnedIdentity) != true) {
            this.bytesOwnedIdentity = bytesOwnedIdentity
        }
    }
    fun initiateFreeTrialQuery() {}
    fun startFreeTrial() {}
    var freeTrialResults: Boolean? by mutableStateOf(null)
    var freeTrialButtonEnabled: Boolean by mutableStateOf(false)
}
