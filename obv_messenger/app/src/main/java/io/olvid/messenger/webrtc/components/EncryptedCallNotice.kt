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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.olvid.messenger.R
import io.olvid.messenger.designsystem.icons.OlvidLogo
import io.olvid.messenger.designsystem.icons.OlvidLogoSize
import io.olvid.messenger.designsystem.theme.OlvidTypography

@Composable
fun EncryptedCallNotice(modifier: Modifier = Modifier, color: Color) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        OlvidLogo(size = OlvidLogoSize.SMALL)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(id = R.string.call_encrypted_notice),
            style = OlvidTypography.body2,
            color = color
        )
    }
}

@Preview
@Composable
private fun EncryptedCallNoticePreview() {
    EncryptedCallNotice(color = Color.White)
}
