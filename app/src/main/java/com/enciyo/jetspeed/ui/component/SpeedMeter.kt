package com.enciyo.jetspeed.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.enciyo.jetspeed.R

@Composable
fun SpeedMeter(progress: Float) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_speed_meters))
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            LottieProperty.COLOR,
            MaterialTheme.colorScheme.primary.toArgb(),
            "Shape Layer 1",
            "**",
        ),
        rememberLottieDynamicProperty(
            LottieProperty.COLOR,
            MaterialTheme.colorScheme.primary.toArgb(),
            "Path 141",
            "**",
        ),
        rememberLottieDynamicProperty(
            LottieProperty.COLOR,
            MaterialTheme.colorScheme.secondaryContainer.toArgb(),
            "Speed",
            "**",
        ),
    )
    LottieAnimation(
        composition = composition,
        dynamicProperties = dynamicProperties,
        progress = { progress },
    )
}
