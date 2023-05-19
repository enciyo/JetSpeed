@file:OptIn(ExperimentalMaterialApi::class)

package com.enciyo.jetspeed.ui.speed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.enciyo.jetspeed.ui.component.SpeedMeter


@Composable
fun SpeedRoute(
    vm: SpeedViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()
    SpeedContent(state = state, onRetry = {
        vm.retry()
    })
}

@Composable
fun SpeedContent(
    modifier: Modifier = Modifier,
    state: SpeedUiState,
    onRetry: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpeedInfoContent(state = state)
        AnimatedVisibility(visible = state.isCompleted.not()) {
            SpeedMeter(progress = state.progress)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = state.speedText)
        if (state.isCompleted)
            Button(onClick = onRetry) {
                Text(text = "Retry")
            }
    }
}


@Composable
fun SpeedInfoContent(
    modifier: Modifier = Modifier,
    state: SpeedUiState
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SpeedInfoItem(
            modifier = Modifier.weight(1f),
            title = "Download",
            value = state.downloadTime,
            textAlign = TextAlign.End
        )
        SpeedInfoItem(
            modifier = Modifier.weight(1f),
            title = "Upload",
            value = state.uploadTime,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun SpeedInfoItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    textAlign: TextAlign
) {
    ListItem(
        modifier = modifier,
        text = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = textAlign
            )
        },
        secondaryText = {
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                textAlign = textAlign
            )
        },
    )
}


@Preview
@Composable
fun SpeedPreview() {
    SpeedContent(
        state = SpeedUiState(
            uploadTime = "",
            downloadTime = "",
            speedText = "",
            progress = 0.3f
        ),
        onRetry = {}
    )
}