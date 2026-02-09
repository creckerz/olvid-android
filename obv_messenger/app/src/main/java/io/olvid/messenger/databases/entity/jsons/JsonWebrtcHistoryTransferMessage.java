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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonWebrtcHistoryTransferMessage {
    @JsonProperty("ice")
    public List<JsonWebrtcHistoryTransferIceCandidate> iceCandidates;
    @JsonProperty("sdp")
    public JsonWebrtcHistoryTransferSdp sdp;

    public JsonWebrtcHistoryTransferMessage() {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JsonWebrtcHistoryTransferIceCandidate {
        @JsonProperty("sdp")
        public String sdp;
        @JsonProperty("mli")
        public int sdpMLineIndex;
    
        public JsonWebrtcHistoryTransferIceCandidate() {
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JsonWebrtcHistoryTransferSdp {
        @JsonProperty("t")
        public String type; // "offer" or "answer"
        @JsonProperty("sdp")
        public String sdp;

        public JsonWebrtcHistoryTransferSdp() {
        }
    }
}
