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

package io.olvid.messenger.discussion

import androidx.fragment.app.FragmentManager
import io.olvid.messenger.App
import io.olvid.messenger.customClasses.BytesKey
import io.olvid.messenger.databases.AppDatabase
import io.olvid.messenger.fragments.dialog.MultiCallStartDialogFragment

class CallHandler(
    private val activity: DiscussionActivity,
    private val supportFragmentManager: FragmentManager
) {
    fun onCallButtonClicked(callLogItemId: Long) {
        App.runThread {
            val callLogItem =
                AppDatabase.getInstance().callLogItemDao()[callLogItemId]
            if (callLogItem != null) {
                if (callLogItem.contacts.size == 1 && callLogItem.callLogItem.bytesGroupOwnerAndUidOrIdentifier == null) {
                    if (callLogItem.oneContact != null && callLogItem.oneContact.hasChannelOrPreKey()) {
                        App.startWebrtcCall(
                            activity,
                            callLogItem.oneContact.bytesOwnedIdentity,
                            callLogItem.oneContact.bytesContactIdentity
                        )
                    }
                } else {
                    val bytesContactIdentities =
                        ArrayList<BytesKey>(callLogItem.contacts.size)
                    for (callLogItemContactJoin in callLogItem.contacts) {
                        bytesContactIdentities.add(BytesKey(callLogItemContactJoin.bytesContactIdentity))
                    }
                    val multiCallStartDialogFragment =
                        MultiCallStartDialogFragment.newInstance(
                            callLogItem.callLogItem.bytesOwnedIdentity,
                            callLogItem.callLogItem.bytesGroupOwnerAndUidOrIdentifier,
                            bytesContactIdentities
                        )
                    multiCallStartDialogFragment.show(
                        supportFragmentManager,
                        "dialog"
                    )
                }
            }
        }
    }
}