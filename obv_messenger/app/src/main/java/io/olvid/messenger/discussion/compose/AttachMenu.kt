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

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.olvid.messenger.R
import io.olvid.messenger.customClasses.DiscussionInputEditText
import io.olvid.messenger.designsystem.components.OlvidDropdownMenu
import io.olvid.messenger.designsystem.components.OlvidDropdownMenuItem

enum class AttachMenuitem(val stringRes: Int, val drawableRes: Int) {
    ATTACH_POLL(R.string.label_poll_create, R.drawable.ic_attach_poll),
    ATTACH_TIMER(R.string.label_attach_timer, R.drawable.ic_ephemeral),
    ATTACH_LOCATION(R.string.label_send_your_location, R.drawable.ic_attach_location),
    ATTACH_EMOJI(R.string.label_attach_emoji, R.drawable.ic_attach_emoji),
    ATTACH_INTRODUCE(R.string.button_label_introduce, R.drawable.ic_attach_introduce),
    ATTACH_VIDEO(R.string.label_attach_video, R.drawable.ic_attach_video),
    ATTACH_CAMERA(R.string.label_attach_camera, R.drawable.ic_attach_camera),
    ATTACH_IMAGE(R.string.label_attach_image, R.drawable.ic_attach_image),
    ATTACH_FILE(R.string.label_attach_file, R.drawable.ic_attach_file),
}

@Composable
fun AttachMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    hasCamera: Boolean,
    showContactIntroduction: Boolean,
    inputEditText: DiscussionInputEditText?,
    controller: ComposeMessageController
) {
    OlvidDropdownMenu(
        modifier = Modifier,
        expanded = expanded,
        onDismissRequest = onDismiss,
    ) {
        AttachMenuitem.entries.filter { action ->
            when(action) {
                AttachMenuitem.ATTACH_CAMERA,
                AttachMenuitem.ATTACH_VIDEO -> hasCamera
                AttachMenuitem.ATTACH_INTRODUCE -> showContactIntroduction
                AttachMenuitem.ATTACH_EMOJI -> false // since emoji keyboard toggle is always shown, remove completely if validated
                else -> true
            }
        }.forEach { item ->
            OlvidDropdownMenuItem(
                text = stringResource(item.stringRes),
                trailingIcon = {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(item.drawableRes),
                        tint = colorResource(R.color.almostBlack),
                        contentDescription = stringResource(item.stringRes)
                    )
                },
                onClick = {
                    when (item) {
                        AttachMenuitem.ATTACH_TIMER -> {
                            controller.onAttachTimer()
                        }

                        AttachMenuitem.ATTACH_IMAGE -> {
                            controller.onAttachImage()
                        }

                        AttachMenuitem.ATTACH_FILE -> {
                            controller.onAttachFile()
                        }

                        AttachMenuitem.ATTACH_VIDEO -> {
                            controller.onAttachVideo(hasCamera)
                        }

                        AttachMenuitem.ATTACH_POLL -> {
                            controller.onAttachPoll()
                        }

                        AttachMenuitem.ATTACH_CAMERA -> {
                            controller.onAttachCamera(hasCamera)
                        }

                        AttachMenuitem.ATTACH_EMOJI -> {
                            controller.onAttachEmoji(inputEditText)
                        }

                        AttachMenuitem.ATTACH_LOCATION -> {
                            controller.onAttachLocation()
                        }

                        AttachMenuitem.ATTACH_INTRODUCE -> {
                            controller.onAttachIntroduce()
                        }
                    }
                    onDismiss()
                }
            )
        }
    }
}