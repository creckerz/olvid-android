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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.olvid.messenger.R
import io.olvid.messenger.designsystem.theme.OlvidTypography

@Composable
fun ExpandablePersonalNote(modifier: Modifier = Modifier, personalNote: String, contentColor: Color = Color.White) {
    if (personalNote.isNotEmpty()) {
        var expanded by rememberSaveable { mutableStateOf(false) }
        val rotation: Float by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
        Column(
            modifier = modifier
                .width(IntrinsicSize.Max)
                .widthIn(max = 300.dp)
                .animateContentSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false)
                ) {
                    expanded = !expanded
                }
                .border(
                    width = 1.dp,
                    color = contentColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(start = 12.dp, end = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.hint_personal_note),
                    style = OlvidTypography.body2,
                    color = contentColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    modifier = Modifier.rotate(degrees = rotation).size(24.dp).padding(2.dp),
                    painter = painterResource(R.drawable.ic_chevron_down),
                    tint = contentColor,
                    contentDescription = null
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 100.dp)
                        .padding(top = 8.dp, end = 6.dp)
                        .verticalScroll(state = rememberScrollState())
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = personalNote,
                        style = OlvidTypography.body1,
                        color = contentColor.copy(alpha = .7f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ExpandablePersonalNotePreview() {
    ExpandablePersonalNote(
        personalNote = "This is a personal note for the preview. It can be quite long to test the scrolling behavior."
    )
}
