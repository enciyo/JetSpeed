@file:OptIn(ExperimentalMaterialApi::class)

package com.enciyo.jetspeed.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.domain.model.Server
import kotlinx.coroutines.launch

@Composable
fun ServersModalBottomSheet(
    modifier: Modifier = Modifier,
    servers: List<Server>,
    onChangeServer: (Server) -> Unit,
    content: @Composable () -> Unit,
    state: ModalBottomSheetState,
) {

    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = state,
        sheetShape = RoundedCornerShape(topStartPercent = 5, topEndPercent = 5),
        modifier = modifier,
        sheetContent = { ServersContent(servers = servers, onChangeServer = onChangeServer.also {
            scope.launch { state.hide() }
        }) },
        content = content
    )
}

@Composable
fun ServersContent(
    modifier: Modifier = Modifier,
    servers: List<Server>,
    onChangeServer: (Server) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                TopLine(modifier = Modifier.align(Alignment.Center))
            }
        }
        items(servers) { server ->
            ServerItemContent(server = server, onChangeServer = onChangeServer)
        }
    }
}


@Composable
fun ServerItemContent(
    modifier: Modifier = Modifier,
    server: Server,
    onChangeServer: (Server) -> Unit,
) {
    ListItem(
        modifier = modifier.clickable(onClick = { onChangeServer(server) }),
        text = {
            Text(
                text = server.name + "/" + server.country,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        secondaryText = {
            Text(
                text = server.sponsor,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        trailing = {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowRight,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = ""
            )
        }
    )
}

@Composable
fun TopLine(modifier: Modifier) {
    Divider(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .size(width = 32.dp, height = 4.dp)
            .background(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
    )
}