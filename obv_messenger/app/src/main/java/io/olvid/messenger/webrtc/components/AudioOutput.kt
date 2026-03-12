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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.olvid.messenger.R
import io.olvid.messenger.webrtc.WebrtcCallService.AudioOutput
import io.olvid.messenger.webrtc.WebrtcCallService.AudioOutput.BLUETOOTH
import io.olvid.messenger.webrtc.WebrtcCallService.AudioOutput.HEADSET
import io.olvid.messenger.webrtc.WebrtcCallService.AudioOutput.LOUDSPEAKER
import io.olvid.messenger.webrtc.WebrtcCallService.AudioOutput.MUTED
import io.olvid.messenger.webrtc.WebrtcCallService.AudioOutput.PHONE

fun AudioOutput.drawableResource() = when (this) {
    PHONE -> R.drawable.ic_phone_grey
    HEADSET -> R.drawable.ic_headset_grey
    LOUDSPEAKER -> R.drawable.ic_speaker_grey
    BLUETOOTH -> R.drawable.ic_speaker_bluetooth_grey
    MUTED -> R.drawable.ic_speaker_off
}

private fun AudioOutput.stringResource() = when (this) {
    PHONE -> R.string.text_audio_output_phone
    HEADSET -> R.string.text_audio_output_headset
    LOUDSPEAKER -> R.string.text_audio_output_loudspeaker
    BLUETOOTH -> R.string.text_audio_output_bluetooth
    MUTED -> R.string.text_audio_output_no_sound
}

@Composable
fun AudioOutput.Composable() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = this@Composable.drawableResource()),
            tint = colorResource(R.color.greyTint),
            contentDescription = name,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = stringResource(id = this@Composable.stringResource()),
            color = colorResource(R.color.alwaysWhite)
        )
    }
}