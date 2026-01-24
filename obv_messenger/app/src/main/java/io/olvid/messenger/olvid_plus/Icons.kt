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

package io.olvid.messenger.olvid_plus

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.olvid.messenger.R

@Composable
fun OlvidPlusMultiDevice(modifier: Modifier = Modifier.size(32.dp)) {
    Icon(
        modifier = modifier.background(color = colorResource(R.color.pink), shape = CircleShape),
        painter = painterResource(R.drawable.olvid_plus_multi_device),
        tint = colorResource(R.color.alwaysWhite),
        contentDescription = null
    )
}

@Composable
fun OlvidPlusCall(modifier: Modifier = Modifier.size(32.dp)) {
    Icon(
        modifier = modifier.background(color = colorResource(R.color.orange), shape = CircleShape),
        painter = painterResource(R.drawable.olvid_plus_call),
        tint = colorResource(R.color.alwaysWhite),
        contentDescription = null
    )
}

@Composable
fun OlvidPlusSupport(modifier: Modifier = Modifier.size(32.dp)) {
    Icon(
        modifier = modifier.background(color = colorResource(R.color.red), shape = CircleShape),
        painter = painterResource(R.drawable.olvid_plus_support),
        tint = colorResource(R.color.alwaysWhite),
        contentDescription = null
    )
}


@Preview
@Composable
private fun OlvidPlusIconsPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.olvid_plus_logo),
            contentDescription = null
        )
        OlvidPlusMultiDevice()
        OlvidPlusCall()
        OlvidPlusSupport()
    }
}