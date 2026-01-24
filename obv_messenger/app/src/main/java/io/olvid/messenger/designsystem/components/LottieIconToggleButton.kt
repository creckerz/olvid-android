package io.olvid.messenger.designsystem.components

import androidx.annotation.ColorRes
import androidx.annotation.RawRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import io.olvid.messenger.R

/**
 * An icon button that uses Lottie animations to transition between two states.
 *
 * @param modifier The modifier to be applied to the button.
 * @param toggled The state of the button. If true, the button will be in the "toggled" state.
 * @param onClick The callback to be invoked when the button is clicked.
 */
@Composable
fun LottieIconToggleButton(
    modifier: Modifier = Modifier,
    @RawRes rawRes: Int = R.raw.smile_to_keyboard,
    @ColorRes colorRes: Int = R.color.almostBlack,
    toggled: Boolean,
    onClick: () -> Unit,
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(rawRes)
    )

    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                colorResource(colorRes).toArgb(),
                BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf("**")
        )
    )
    var isPlaying by remember { mutableStateOf(false) }
    val progress by animateLottieCompositionAsState(
        isPlaying = isPlaying,
        composition = composition,
        speed = if (toggled) 1f else -1f,
        restartOnPlay = false
    )

    Box(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = 32.dp, color = colorResource(R.color.almostBlack)),
                onClick = {
                    isPlaying = true
                    onClick()
                }
            )
    ) {
        LottieAnimation(
            composition = composition,
            dynamicProperties = dynamicProperties,
            progress = { progress },
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun LottieIconToggleButtonPreview() {
    var toggled by remember { mutableStateOf(false) }
    LottieIconToggleButton(
        modifier = Modifier.size(40.dp),
        toggled = toggled,
        onClick = { toggled = !toggled }
    )
}
