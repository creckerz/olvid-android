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

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import io.olvid.messenger.R
import io.olvid.messenger.designsystem.theme.OlvidTypography
import io.olvid.messenger.main.contacts.ContactListScreen
import io.olvid.messenger.main.contacts.ContactListViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddParticipantScreen(
    contactListViewModel: ContactListViewModel,
    onSelectionDone: () -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    Text(
        modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 16.dp),
        text = stringResource(id = R.string.webrtc_add_participants),
        style = OlvidTypography.h2.copy(
            fontWeight = FontWeight.Medium
        ),
        color = Color.White,
    )
    var textFieldValue: TextFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                contactListViewModel.getFilter().orEmpty()
            )
        )
    }
    Box(modifier = Modifier.padding(start = 20.dp, end = 24.dp)) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .border(
                    width = 1.dp,
                    color = Color(0xFF39393D),
                    shape = RoundedCornerShape(size = 16.dp)
                )
                .padding(12.dp),
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                contactListViewModel.setFilter(it.text)
            },
            singleLine = true,
            textStyle = OlvidTypography.body1.copy(
                color = Color.White,
            ),
            cursorBrush = SolidColor(Color.White),
        )
        if (contactListViewModel.getFilter().isNullOrEmpty()) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 12.dp),
                text = stringResource(id = R.string.hint_search_contact_name),
                style = OlvidTypography.body1,
                color = Color(0xFF8B8D97),
            )
        }
        Icon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp),
            painter = painterResource(id = R.drawable.ic_search),
            tint = Color(0xFF39393D),
            contentDescription = "search"
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    ContactListScreen(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .imePadding()
            .navigationBarsPadding(),
        contactListViewModel = contactListViewModel,
        refreshing = false,
        onRefresh = null,
        selectable = true,
        onClick = {
            textFieldValue =
                textFieldValue.copy(selection = TextRange(0, textFieldValue.text.length))
        },
        onSelectionDone = {
            keyboard?.hide()
            onSelectionDone()
        },
        onInvite = { },
        onScrollStart = { keyboard?.hide() }
    )
}
