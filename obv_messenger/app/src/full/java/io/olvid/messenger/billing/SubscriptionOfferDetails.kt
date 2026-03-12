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
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.billingclient.api.ProductDetails
import io.olvid.messenger.App
import io.olvid.messenger.R
import io.olvid.messenger.designsystem.theme.OlvidTypography


data class SubscriptionOfferDetails(
    val title: String,
    val description: String?,
    val details: ProductDetails,
    val offerToken: String,
    val pricingPhase: String,
    val price: String,
    val priceMicros: Long
)

fun SubscriptionOfferDetails.formattedPrice(): String {
    return when (pricingPhase) {
        "P1Y" -> App.getContext().getString(R.string.button_label_price_per_year, price)
        else -> App.getContext().getString(R.string.button_label_price_per_month, price)
    }
}

@Composable
fun SubscriptionOfferDetails(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    discount: String? = null,
    selected: Boolean = true,
    onSelected: () -> Unit = {}
) {
    val topLeftGradient by animateColorAsState(if (selected) Color(0xFF6BB700) else Color.Transparent)
    val bottomRightGradient by animateColorAsState(if (selected) colorResource(id = R.color.olvid_gradient_light) else Color.Transparent)

    Box {
        Card(
            modifier = modifier
                .widthIn(min = 165.dp)
                .padding(top = 9.dp),
            border = BorderStroke(
                width = 2.dp,
                brush = Brush.linearGradient(colors = listOf(topLeftGradient, bottomRightGradient))
            ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.backgroundOverDialogBackground),
                contentColor = colorResource(R.color.almostBlack)
            ),
            onClick = onSelected
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = OlvidTypography.h2,
                    color = colorResource(R.color.almostBlack)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = OlvidTypography.body2,
                    color = colorResource(R.color.greyTint)
                )
            }
        }
        discount?.let { discount ->
            Text(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top= 4.dp, end = 8.dp)
                    .background(
                        colorResource(R.color.olvid_gradient_dark),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 2.dp),
                text = discount,
                style = OlvidTypography.caption,
                color = colorResource(R.color.alwaysWhite)
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SubscriptionOfferDetailsPreview() {
    SubscriptionOfferDetails(
        title = "Mensuel",
        description = "4,99€/mois",
        discount = "Économisez 20%"
    ) { }
}