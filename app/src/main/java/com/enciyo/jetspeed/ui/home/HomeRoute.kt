@file:OptIn(ExperimentalMaterialApi::class)

package com.enciyo.jetspeed.ui.home

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.enciyo.jetspeed.R
import com.enciyo.jetspeed.Screens
import com.enciyo.jetspeed.ui.component.CircleButton
import com.enciyo.jetspeed.ui.theme.JetSpeedTheme
import com.example.domain.model.Server
import com.example.emptyFunction
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    vm: HomeViewModel = hiltViewModel(),
    onNavigateSpeedRoute: () -> Unit
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val stateSheet = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val onClickChangeServer: () -> Unit = remember(key1 = vm) {
        { scope.launch { stateSheet.show() } }
    }

    val onChangeServer: (Server) -> Unit = remember(key1 = vm) {
        { vm.onEvent(HomeScreenInteractions.OnSelected(it)) }
    }

    ServersModalBottomSheet(
        servers = state.servers,
        state = stateSheet,
        content = {
            HomeContent(
                state = state,
                onClickChangeServer = onClickChangeServer,
                onClickStart = onNavigateSpeedRoute
            )
        },
        onChangeServer = onChangeServer,
    )
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    onClickChangeServer: () -> Unit,
    onClickStart: () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircleButton(
                onClick = onClickStart,
                text = stringResource(id = R.string.go),
            )
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(visible = state.currentServer != null) {
                SelectableServerInfoContent(
                    server = state.currentServer!!,
                    onChangeServer = onClickChangeServer
                )
            }
        }
    }
}

@Composable
fun SelectableServerInfoContent(
    server: Server,
    onChangeServer: () -> Unit,
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
                text = stringResource(id = R.string.server_title, server.name, server.country),
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
            onClick = onChangeServer,
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
        ) {
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
        HomeContent(
            state = HomeUiState(),
            onClickStart = emptyFunction(),
            onClickChangeServer = emptyFunction()
        )
    }
}
