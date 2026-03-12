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

package io.olvid.messenger.webrtc.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.olvid.messenger.R
import io.olvid.messenger.designsystem.components.OlvidDropdownMenu
import io.olvid.messenger.webrtc.WebrtcCallService.AudioOutput
import io.olvid.messenger.webrtc.WebrtcCallService.AudioOutput.BLUETOOTH


@Composable
fun SpeakerToggleButton(drawableRes: Int, onClick: () -> Unit) {
    Row(
        Modifier
            .width(60.dp)
            .height(32.dp)
            .background(color = Color(0xFF29282D), shape = RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
            .clickable { onClick() }
            .padding(start = 14.dp, top = 8.dp, bottom = 8.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = drawableRes),
            tint = Color.White,
            contentDescription = ""
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_down),
            tint = Color.White,
            contentDescription = ""
        )
    }
}

@Composable
fun SpeakerToggle(
    modifier: Modifier = Modifier,
    audioOutputs: List<AudioOutput>,
    onToggleSpeaker: (audioOutput: AudioOutput) -> Unit,
    content: @Composable (onClick: () -> Unit) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Box(modifier = modifier) {
        content { expanded = true }
        OlvidDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            audioOutputs.forEach { audioOutput ->
                DropdownMenuItem(
                    onClick = {
                        onToggleSpeaker(audioOutput)
                        expanded = false
                    }
                ) {
                    audioOutput.Composable()
                }
            }
        }
    }
}

@Preview
@Composable
fun SpeakerTogglePreview() {
    SpeakerToggle(audioOutputs = listOf(BLUETOOTH), onToggleSpeaker = {}) {
        SpeakerToggleButton(R.drawable.ic_speaker_light_grey, it)
    }
}
