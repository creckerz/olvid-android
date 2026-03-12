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

package io.olvid.messenger.billing

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.olvid.messenger.R
import io.olvid.messenger.designsystem.components.OlvidTextButton
import io.olvid.messenger.designsystem.theme.OlvidTypography

@Composable
fun FreeTrialView(enabled: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                BorderStroke(
                    width = 2.dp,
                    brush = Brush.linearGradient(colors = listOf(Color(0xFF6BB700), colorResource(id = R.color.olvid_gradient_light))
                    )),
                    RoundedCornerShape(16.dp))
            .background(colorResource(R.color.lightGrey))
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.label_free_trial_available),
            style = OlvidTypography.h3,
            color = colorResource(R.color.almostBlack)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.label_free_trial_explanation),
            style = OlvidTypography.body2,
            color = colorResource(R.color.greyTint)
        )
        Spacer(Modifier.height(8.dp))
        OlvidTextButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.button_label_start_free_trial),
            onClick = onClick,
            enabled = enabled,
            contentColor = colorResource(R.color.olvid_gradient_light)
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FreeTrialViewPreview() {
    Column(modifier = Modifier.background(colorResource(R.color.newDialogBackground)).padding(16.dp)) {
        FreeTrialView(
            enabled = true,
            onClick = {}
        )
    }
}
