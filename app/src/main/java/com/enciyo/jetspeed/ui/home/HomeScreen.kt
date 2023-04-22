@file:OptIn(ExperimentalMaterialApi::class)

package com.enciyo.jetspeed.ui.home

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
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
    val scope = rememberCoroutineScope()
    ServerListModalBottomSheet(
        servers = state.servers,
        modifier = modifier,
        content = {
            HomeScreen(
                modifier = Modifier.fillMaxSize(),
                state = state,
                onClickChangeServer = {
                    scope.launch { it.show() }
                },
                onClickStart = {
                    vm.getDownloadSpeedTest()
                }
            )
        },
        onSelected = {
            vm.onSelected(it)
        }
    )
}


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
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
            StartButton(
                onClick = onClickStart,
                text  = state.text
            )
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(visible = server != null) {
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

    Text(
        text = onText,
        modifier = modifier
            .clip(shape = CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick)
            .size(180.dp)
            .wrapContentSize(align = Alignment.Center),
        color = MaterialTheme.colorScheme.onPrimary,
        style = if (text.isEmpty()) MaterialTheme.typography.displayLarge else MaterialTheme.typography.titleMedium
    )
}

@Composable
fun ServerInfo(
    server: Server,
    onClickChangeServer: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .width(220.dp)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = server.name + "/" + server.country,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = server.sponsor,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        TextButton(onClick = onClickChangeServer) {
            Text(text = stringResource(R.string.change_server))
        }
    }
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
            onClickChangeServer = {}
        )
    }
}
