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

package io.olvid.messenger.customClasses

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import io.olvid.messenger.App
import io.olvid.messenger.R
import io.olvid.messenger.activities.ObvLinkActivity
import io.olvid.messenger.designsystem.components.BaseDialogContent
import io.olvid.messenger.designsystem.components.DialogSecure
import io.olvid.messenger.designsystem.components.OlvidTextButton
import io.olvid.messenger.designsystem.showDialog
import io.olvid.messenger.designsystem.theme.OlvidTypography
import io.olvid.messenger.designsystem.theme.olvidSwitchDefaults
import io.olvid.messenger.discussion.DiscussionActivity
import io.olvid.messenger.main.MainActivity

object LinkUtils {
    @JvmStatic
    fun openLink(context: Context?, uri: Uri?) {
        if (context == null || uri == null) {
            return
        }
        context.showDialog { onDismiss ->
            OpenLinkDialog(
                uri = uri,
                onDismiss = onDismiss
            )
        }
    }

    @JvmStatic
    fun displayText(context: Context?, text: String?) {
        if (context == null || text.isNullOrEmpty()) {
            return
        }
        context.showDialog { onDismiss ->
            TextDisplayDialog(
                text = text,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
fun OpenLinkDialog(
    uri: Uri,
    onDismiss: () -> Unit
) {
    DialogSecure(onDismissRequest = onDismiss) {
        OpenLinkContent(
            uri = uri,
            onDismiss = onDismiss,
        )
    }
}

@Composable
fun TextDisplayDialog(
    text: String,
    onDismiss: () -> Unit
) {
    DialogSecure(onDismissRequest = onDismiss) {
        TextDisplayContent(
            text = text,
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun TextDisplayContent(
    text: String,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    BaseDialogContent(
        title = stringResource(R.string.label_text_qr_code),
        content = {
            SelectionContainer {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = text,
                    style = OlvidTypography.body1,
                    color = colorResource(R.color.greyTint)
                )
            }
        },
        actions = {
            OlvidTextButton(
                text = stringResource(R.string.button_label_copy),
                contentColor = colorResource(R.color.olvid_gradient_light),
            ) {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(text, text)
                clipboard.setPrimaryClip(clip)
                App.toast(R.string.toast_message_clipboard_copied, Toast.LENGTH_SHORT)
            }
            Spacer(modifier = Modifier.weight(1f))
            OlvidTextButton(
                text = stringResource(R.string.button_label_ok),
                contentColor = colorResource(R.color.olvid_gradient_light),
                onClick = onDismiss,
            )
        }
    )
}

@Composable
private fun OpenLinkContent(
    uri: Uri,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val olvidLink = rememberSaveable { ObvLinkActivity.ANY_PATTERN.matcher(uri.toString()).find() }
    val cleanUrl = rememberSaveable { uri.clean() }
    val cleanableUrl = rememberSaveable { cleanUrl != uri.toString() }
    var useCleanUrl by rememberSaveable { mutableStateOf(true) }

    BaseDialogContent(
        title = stringResource(if (olvidLink) R.string.dialog_title_confirm_open_olvid_link else R.string.dialog_title_confirm_open_link),
        content = {
            SelectionContainer {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (cleanableUrl && useCleanUrl) cleanUrl else uri.toString(),
                    style = OlvidTypography.body1,
                    color = colorResource(R.color.olvid_gradient_light)
                )
            }

            if (cleanableUrl) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { useCleanUrl = !useCleanUrl }) {
                    Switch(
                        checked = !useCleanUrl,
                        onCheckedChange = { useCleanUrl = !useCleanUrl },
                        colors = olvidSwitchDefaults()
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.label_open_original_link_with_trackers),
                        style = OlvidTypography.body1,
                        color = colorResource(R.color.greyTint)
                    )
                }
            }
        },
        actions = {
            OlvidTextButton(
                text = stringResource(R.string.button_label_copy),
                contentColor = colorResource(R.color.olvid_gradient_light),
            ) {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val textToCopy = if (useCleanUrl) cleanUrl else uri.toString()
                val clip = ClipData.newPlainText(textToCopy, textToCopy)
                clipboard.setPrimaryClip(clip)
                App.toast(R.string.toast_message_link_copied, Toast.LENGTH_SHORT)
                onDismiss()
            }

            Spacer(modifier = Modifier.weight(1f))

            OlvidTextButton(
                text = stringResource(R.string.button_label_cancel),
                contentColor = colorResource(R.color.greyTint),
                onClick = onDismiss
            )

            OlvidTextButton(
                text = stringResource(R.string.button_label_ok),
                contentColor = colorResource(R.color.olvid_gradient_light),
            ) {
                val targetUri = if (useCleanUrl) cleanUrl.toUri() else uri
                if (olvidLink) {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.action = MainActivity.LINK_ACTION
                    intent.putExtra(MainActivity.LINK_URI_INTENT_EXTRA, targetUri.toString())
                    context.startActivity(intent)
                } else {
                    runCatching {
                        context.startActivity(Intent(Intent.ACTION_VIEW, targetUri))

                        var baseContext = context
                        var unwraps = 0
                        while (baseContext !is DiscussionActivity && baseContext is ContextThemeWrapper) {
                            baseContext = baseContext.baseContext
                            unwraps++
                            if (unwraps > 10) break
                        }
                        if (baseContext is DiscussionActivity) {
                            baseContext.discussionViewModel.doNotMarkAsReadOnPause()
                        }

                    }.onFailure {
                        App.toast(R.string.toast_message_unable_to_open_url, Toast.LENGTH_SHORT)
                    }
                }
                onDismiss()
            }
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun OpenLinkContentPreview() {
    OpenLinkContent(
        uri = "https://olvid.io/faq?utm_source=google&utm_medium=cpc".toUri(),
        onDismiss = {},
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun TextDisplayPreview() {
    TextDisplayContent(
        text = "https://olvid.io/faq?utm_source=google&utm_medium=cpc",
        onDismiss = {}
    )
}
