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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AudioLevel(modifier: Modifier = Modifier, audioLevel: Double?) {
    BoxWithConstraints(
        modifier = modifier
            .background(color = Color.White.copy(alpha = .05f), shape = CircleShape)
            .clip(
                CircleShape
            )
    ) {
        val padding = maxWidth / 12
        val width = maxWidth / 3 - padding * 2
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(3) {
                Spacer(
                    modifier = Modifier
                        .background(color = Color.Blue, shape = RoundedCornerShape(12.dp))
                        .width(width)
                        .heightIn(min = width)
                        .fillMaxHeight(
                            (audioLevel?.toFloat() ?: 0f)
                                .times(if (it == 1) 1f else 0.66f)
                                .coerceAtLeast(.1f)
                        )
                )
            }
        }
    }
}

@Composable
@Preview
fun AudioLevelPreview() {
    Row {
        AudioLevel(modifier = Modifier.size(64.dp), audioLevel = 0.0)
        Spacer(modifier = Modifier.width(4.dp))
        AudioLevel(modifier = Modifier.size(64.dp), audioLevel = 1.0)
    }
}
