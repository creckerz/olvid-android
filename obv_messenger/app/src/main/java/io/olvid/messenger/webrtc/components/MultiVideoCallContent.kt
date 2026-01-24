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

package io.olvid.messenger.webrtc.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.olvid.messenger.webrtc.WebrtcCallService
import io.olvid.messenger.webrtc.WebrtcCallService.CallParticipantPojo
import io.olvid.messenger.webrtc.WebrtcPeerConnectionHolder.Companion.localScreenTrack
import io.olvid.messenger.webrtc.WebrtcPeerConnectionHolder.Companion.localVideoTrack


@Composable
fun BoxScope.MultiVideoCallContent(
    participants: List<CallParticipantPojo>,
    webrtcCallService: WebrtcCallService,
    peekHeight: Dp
) {
    val selectedCamera by webrtcCallService.selectedCameraLiveData.observeAsState()
// multi
    Column {
        LazyRow(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 8.dp,
                bottom = 10.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = participants.filterNot {
                it.bytesContactIdentity.contentEquals(
                    webrtcCallService.selectedParticipant
                )
            }) { callParticipant ->
                val remoteVideoTrack =
                    webrtcCallService.getCallParticipant(callParticipant.bytesContactIdentity)?.peerConnectionHolder?.remoteVideoTrack
                Card(
                    modifier = Modifier
                        .size(72.dp)
                        .clickable {
                            webrtcCallService.selectedParticipant =
                                callParticipant.bytesContactIdentity
                        },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    CallParticipant(
                        callParticipant = callParticipant,
                        videoTrack = remoteVideoTrack,
                        audioLevel = webrtcCallService.getCallParticipant(callParticipant.bytesContactIdentity)?.peerConnectionHolder?.peerAudioLevel
                    )

                }

            }
        }
        if (webrtcCallService.selectedParticipant.contentEquals(webrtcCallService.bytesOwnedIdentity!!)) {
            if (webrtcCallService.screenShareActive) {
                ScreenSharingNoticeScreen { webrtcCallService.toggleScreenShare() }
            } else {
                CallParticipant(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 20.dp,
                                topEnd = 20.dp
                            )
                        )
                        .fillMaxSize(),
                    bytesOwnedIdentity = webrtcCallService.bytesOwnedIdentity,
                    mirror = selectedCamera?.mirror == true,
                    videoTrack = localVideoTrack.takeIf { webrtcCallService.cameraEnabled },
                    screenTrack = localScreenTrack.takeIf { webrtcCallService.screenShareActive },
                    zoomable = true,
                    audioLevel = webrtcCallService.getAudioLevel(webrtcCallService.bytesOwnedIdentity)
                )
            }
        } else {
            val remoteVideoTrack =
                webrtcCallService.getCallParticipant(webrtcCallService.selectedParticipant)?.peerConnectionHolder?.remoteVideoTrack
            CallParticipant(
                callParticipant = CallParticipantPojo(
                    webrtcCallService.getCallParticipant(
                        webrtcCallService.selectedParticipant
                    )!!
                ), videoTrack = remoteVideoTrack,
                zoomable = true,
                audioLevel = webrtcCallService.getAudioLevel(webrtcCallService.selectedParticipant)
            )
        }
    }
    if (webrtcCallService.selectedParticipant.contentEquals(webrtcCallService.bytesOwnedIdentity!!)
            .not()
    ) {
        Card(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.BottomEnd)
                .offset(y = -peekHeight)
                .clickable {
                    webrtcCallService.selectedParticipant =
                        webrtcCallService.bytesOwnedIdentity!!
                }
                .padding(end = 16.dp, bottom = 8.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            CallParticipant(
                bytesOwnedIdentity = webrtcCallService.bytesOwnedIdentity,
                mirror = selectedCamera?.mirror == true,
                videoTrack = localVideoTrack.takeIf { webrtcCallService.cameraEnabled },
                screenTrack = localScreenTrack.takeIf { webrtcCallService.screenShareActive },
                audioLevel = webrtcCallService.getAudioLevel(webrtcCallService.bytesOwnedIdentity)
            )
        }
    }
}

