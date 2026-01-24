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

package io.olvid.messenger.discussion.compose

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.olvid.messenger.R

@Composable
fun SendButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isAudioMode: Boolean = false,
    onClick: () -> Unit = {}
) {
    val lightGradientColor by animateColorAsState(if (enabled) colorResource(id = R.color.olvid_gradient_light) else colorResource(id = R.color.lightGrey))
    val darkGradientColor by animateColorAsState(if (enabled) colorResource(id = R.color.olvid_gradient_dark) else colorResource(id = R.color.lightGrey))
    val iconColor  by animateColorAsState(if (enabled) colorResource(id = R.color.alwaysWhite ) else colorResource(id = R.color.mediumGrey))
    Box(
        modifier = modifier
            .size(48.dp)
            .padding(4.dp)
            .clickable(
                enabled = enabled,
                indication = ripple(
                    bounded = true,
                    radius = 24.dp,
                    color = colorResource(R.color.almostBlack)
                ),
                interactionSource = remember { MutableInteractionSource() },
                role = Role.Button,
                onClick = onClick
            )
            .background(
                brush = Brush.verticalGradient(colors = listOf(lightGradientColor, darkGradientColor)),
                shape = CircleShape
            )
    ) {
        AnimatedContent(
            modifier = Modifier
                .align(Alignment.Center),
            targetState = isAudioMode,
            transitionSpec = {
                (scaleIn(tween(200)) + fadeIn(tween(200))).togetherWith(
                    scaleOut(tween(200)) + fadeOut(tween(200))
                )
            },
            contentAlignment = Alignment.Center,
            label = "SendButtonIcon"
        ) { showAudioIcon ->
            if (showAudioIcon) {
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    painter = painterResource(R.drawable.ic_audio),
                    tint = iconColor,
                    contentDescription = stringResource(R.string.content_description_send_message)
                )
            } else {
                Icon(
                    modifier = Modifier
                        .padding(top = 2.dp, end = 4.dp)
                        .size(22.dp),
                    painter = painterResource(R.drawable.ic_send_up),
                    tint = iconColor,
                    contentDescription = stringResource(R.string.content_description_send_message)
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun SendButtonPreview() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SendButton()
        SendButton(enabled = false)
        SendButton(isAudioMode = true)
    }
}