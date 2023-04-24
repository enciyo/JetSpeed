@file:OptIn(ExperimentalMaterialApi::class)

package com.enciyo.jetspeed.ui.home

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.enciyo.data.model.Server
import com.enciyo.jetspeed.R
import com.enciyo.jetspeed.ui.theme.JetSpeedTheme
import kotlinx.coroutines.launch


@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    vm: HomeViewModel = viewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val progressState by vm.progressState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    ServerListModalBottomSheet(
        servers = state.servers,
        modifier = modifier,
        content = {
            HomeScreen(
                modifier = Modifier.fillMaxSize(),
                state = state,
                progressState = progressState,
                onClickChangeServer = {
                    scope.launch { it.show() }
                },
                onClickStart = {
                    vm.getDownloadSpeedTest()
                }
            )
        },
        onSelected = vm::onSelected
    )
}


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    progressState: ProgressState,
    onClickChangeServer: () -> Unit,
    onClickStart: () -> Unit,
) {
    val server = state.selectedServer
    Box(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(progressState.text.isNotEmpty()) {
                SpeedMeter(progressState.percent)
            }
            StartButton(
                onClick = onClickStart,
                text = progressState.text
            )
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(visible = server != null && progressState.text.isEmpty()) {
                ServerInfo(server = server!!, onClickChangeServer = onClickChangeServer)
            }
        }
    }
}

@Composable
fun StartButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String
) {

    val onText = text.ifEmpty { stringResource(id = R.string.go) }
    val style =
        if (text.isEmpty()) MaterialTheme.typography.displayLarge else MaterialTheme.typography.titleMedium

    Text(
        text = onText,
        modifier = modifier
            .clip(shape = CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick)
            .size(180.dp)
            .wrapContentSize(align = Alignment.Center),
        color = MaterialTheme.colorScheme.onPrimary,
        style = style,
    )
}

@Composable
fun ServerInfo(
    server: Server,
    onClickChangeServer: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(32.dp)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.primary)
            .padding(12.dp)
            .fillMaxWidth(),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = server.name + "/" + server.country,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = server.sponsor,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        TextButton(
            onClick = onClickChangeServer,
            colors = androidx.compose.material3.ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
        ) {
            Text(text = stringResource(R.string.change_server))
        }
    }
}

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
        progress = {
            progress
        },
    )

}


@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Full Preview", showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    JetSpeedTheme {
        HomeScreen(
            state = HomeScreenState(
                servers = emptyList(),
                loading = false,
                error = "",
                selectedServer = Server().also {
                    it.name = "Deneme"
                    it.sponsor = "sada"
                }
            ),
            onClickStart = {},
            onClickChangeServer = {},
            progressState = ProgressState()
        )
    }
}
