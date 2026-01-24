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

package io.olvid.messenger.webrtc.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import io.olvid.messenger.webrtc.WebrtcCallService;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public final class JsonBatchIceCandidatesMessage extends JsonWebrtcProtocolMessage {
    @JsonProperty("c")
    public List<JsonIceCandidate> iceCandidates;

    @SuppressWarnings("unused")
    public JsonBatchIceCandidatesMessage() {
    }

    public JsonBatchIceCandidatesMessage(List<JsonIceCandidate> iceCandidates) {
        this.iceCandidates = iceCandidates;
    }

    @Override
    @JsonIgnore
    public int getMessageType() {
        return WebrtcCallService.BATCH_ICE_CANDIDATES_MESSAGE_TYPE;
    }
}
