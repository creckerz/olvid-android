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

package io.olvid.messenger.databases.entity.jsons;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonMessage {
    @Nullable public String body;
    @Nullable public long senderSequenceNumber;
    @Nullable public UUID senderThreadIdentifier;
    @Nullable public byte[] groupUid;
    @Nullable public byte[] groupOwner;
    @Nullable public byte[] groupV2Identifier;
    @Nullable public JsonOneToOneMessageIdentifier oneToOneIdentifier; // an array containing bytesOwnedIdentity and bytesContactIdentity for one-to-one discussions --> allows other devices to know in which discussion to put propagated one-to-one messages
    @Nullable public Boolean forwarded;
    @Nullable public Long originalServerTimestamp;
    @Nullable public JsonMessageReference jsonReply;
    @Nullable public JsonExpiration jsonExpiration;
    @Nullable public JsonLocation jsonLocation;
    @Nullable public List<JsonUserMention> jsonUserMentions;
    @Nullable public JsonPoll jsonPoll;


    public JsonMessage(String body) {
        this.body = body;
    }

    public JsonMessage() {
    }

    @Nullable
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @JsonProperty("ssn")
    @Nullable
    public long getSenderSequenceNumber() {
        return senderSequenceNumber;
    }

    @JsonProperty("ssn")
    public void setSenderSequenceNumber(long senderSequenceNumber) {
        this.senderSequenceNumber = senderSequenceNumber;
    }

    @JsonProperty("sti")
    @Nullable
    public UUID getSenderThreadIdentifier() {
        return senderThreadIdentifier;
    }

    @JsonProperty("sti")
    public void setSenderThreadIdentifier(UUID senderThreadIdentifier) {
        this.senderThreadIdentifier = senderThreadIdentifier;
    }

    @JsonProperty("guid")
    @Nullable
    public byte[] getGroupUid() {
        return groupUid;
    }

    @JsonProperty("guid")
    public void setGroupUid(byte[] groupUid) {
        this.groupUid = groupUid;
    }

    @JsonProperty("go")
    @Nullable
    public byte[] getGroupOwner() {
        return groupOwner;
    }

    @JsonProperty("go")
    public void setGroupOwner(byte[] groupOwner) {
        this.groupOwner = groupOwner;
    }

    @JsonProperty("gid2")
    @Nullable
    public byte[] getGroupV2Identifier() {
        return groupV2Identifier;
    }

    @JsonProperty("gid2")
    public void setGroupV2Identifier(byte[] groupV2Identifier) {
        this.groupV2Identifier = groupV2Identifier;
    }

    @JsonProperty("o2oi")
    @Nullable
    public JsonOneToOneMessageIdentifier getOneToOneIdentifier() {
        return oneToOneIdentifier;
    }

    @JsonProperty("o2oi")
    public void setOneToOneIdentifier(JsonOneToOneMessageIdentifier oneToOneIdentifier) {
        this.oneToOneIdentifier = oneToOneIdentifier;
    }

    @JsonProperty("fw")
    @Nullable
    public Boolean isForwarded() {
        return forwarded;
    }

    @JsonProperty("fw")
    public void setForwarded(Boolean forwarded) {
        this.forwarded = forwarded;
    }

    @JsonProperty("ost")
    @Nullable
    public Long getOriginalServerTimestamp() {
        return originalServerTimestamp;
    }

    @JsonProperty("ost")
    public void setOriginalServerTimestamp(Long originalServerTimestamp) {
        this.originalServerTimestamp = originalServerTimestamp;
    }

    @JsonProperty("re")
    @Nullable
    public JsonMessageReference getJsonReply() {
        return jsonReply;
    }

    @JsonProperty("re")
    public void setJsonReply(JsonMessageReference jsonMessageReference) {
        this.jsonReply = jsonMessageReference;
    }

    @JsonProperty("exp")
    @Nullable
    public JsonExpiration getJsonExpiration() {
        return jsonExpiration;
    }

    @JsonProperty("exp")
    public void setJsonExpiration(JsonExpiration jsonExpiration) {
        this.jsonExpiration = jsonExpiration;
    }

    @JsonProperty("loc")
    @Nullable
    public JsonLocation getJsonLocation() {
        return jsonLocation;
    }

    @JsonProperty("loc")
    public void setJsonLocation(JsonLocation jsonLocation) {
        this.jsonLocation = jsonLocation;
    }

    @JsonProperty("um")
    @Nullable
    public List<JsonUserMention> getJsonUserMentions() {
        return jsonUserMentions;
    }

    @JsonProperty("um")
    public void setJsonUserMentions(@Nullable List<JsonUserMention> jsonUserMentions) {
        this.jsonUserMentions = jsonUserMentions;
    }

    @JsonProperty("p")
    @Nullable
    public JsonPoll getJsonPoll() {
        return jsonPoll;
    }

    @JsonProperty("p")
    public void setJsonPoll(@Nullable JsonPoll jsonPoll) {
        this.jsonPoll = jsonPoll;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return (body == null || body.trim().isEmpty()) && jsonReply == null && jsonLocation == null && jsonPoll == null;
    }

    @JsonIgnore
    public void sanitizeJsonUserMentions() {
        if (jsonUserMentions != null) {
            ArrayList<JsonUserMention> sanitizedMentions = new ArrayList<>();
            for (JsonUserMention mention : jsonUserMentions) {
                if (mention.getUserIdentifier() != null) {
                    sanitizedMentions.add(mention);
                }
            }
            jsonUserMentions = sanitizedMentions;
        }
    }

    @JsonIgnore
    public void setGroupOwnerAndUid(byte[] bytesGroupOwnerAndUid) throws Exception {
        if (bytesGroupOwnerAndUid.length < 32) {
            throw new Exception();
        }
        byte[] bytesGroupOwner = Arrays.copyOfRange(bytesGroupOwnerAndUid, 0, bytesGroupOwnerAndUid.length - 32);
        byte[] bytesGroupUid = Arrays.copyOfRange(bytesGroupOwnerAndUid, bytesGroupOwnerAndUid.length - 32, bytesGroupOwnerAndUid.length);
        setGroupOwner(bytesGroupOwner);
        setGroupUid(bytesGroupUid);
    }
}
