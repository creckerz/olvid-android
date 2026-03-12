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

package io.olvid.messenger.history_transfer.json

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.olvid.messenger.databases.AppDatabase
import io.olvid.messenger.databases.entity.Discussion


@JsonIgnoreProperties(ignoreUnknown = true)
class JsonDiscussionIdentifier() {
    @JsonProperty("t")
    var type: Int? = null
    @JsonProperty("id")
    var identifier: ByteArray? = null


    @JsonIgnore
    constructor(discussion: Discussion) : this() {
        type = when(discussion.discussionType) {
            Discussion.TYPE_CONTACT -> TYPE_CONTACT
            Discussion.TYPE_GROUP -> TYPE_GROUP
            else -> TYPE_GROUP_V2
        }
        identifier = discussion.bytesDiscussionIdentifier
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JsonDiscussionIdentifier) return false

        if (type != other.type) return false
        if (!identifier.contentEquals(other.identifier)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type ?: 0
        result = 31 * result + (identifier?.contentHashCode() ?: 0)
        return result
    }

    @JsonIgnore
    fun getDiscussion(db: AppDatabase, bytesOwnedIdentity: ByteArray): Discussion? {
        return identifier?.let {
             when (type) {
                TYPE_CONTACT -> db.discussionDao().getByContactWithAnyStatus(bytesOwnedIdentity, it)
                TYPE_GROUP -> db.discussionDao().getByGroupOwnerAndUidWithAnyStatus(bytesOwnedIdentity, it)
                TYPE_GROUP_V2 -> db.discussionDao().getByGroupIdentifierWithAnyStatus(bytesOwnedIdentity, it)
                else -> null
            }
        }
    }

    @JsonIgnore
    fun getDiscussionType(): Int? {
        return when(type) {
            TYPE_CONTACT -> Discussion.TYPE_CONTACT
            TYPE_GROUP -> Discussion.TYPE_GROUP
            TYPE_GROUP_V2 -> Discussion.TYPE_GROUP_V2
            else -> null
        }
    }

    companion object {
        const val TYPE_CONTACT = 1
        const val TYPE_GROUP = 2
        const val TYPE_GROUP_V2 = 3
    }
}