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
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.enciyo.data.model.Server
import kotlinx.coroutines.launch

@Composable
fun ServerListModalBottomSheet(
    servers: List<Server>,
    onSelected: (Server) -> Unit,
    content: @Composable (ModalBottomSheetState) -> Unit,
    modifier: Modifier = Modifier,
) {

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStartPercent = 5, topEndPercent = 5),
        modifier = modifier,
        sheetContent = {
            ServerList(servers, onClick = {
                onSelected.invoke(it)
                scope.launch {
                    sheetState.hide()
                }
            })
        },
        content = {
            content.invoke(sheetState)
        }
    )
}

@Composable
fun ServerList(
    servers: List<Server>,
    onClick: (Server) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                DragView(modifier = Modifier.align(Alignment.Center))
            }
        }
        items(servers) { server ->
            ServerListItem(server = server, onClick)
        }
    }
}


@Composable
fun ServerListItem(
    server: Server,
    onClick: (Server) -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier.clickable(onClick = {
            onClick.invoke(server)
        }),
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
fun DragView(modifier: Modifier) {
    Divider(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .size(width = 32.dp, height = 4.dp)
            .background(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
    )
}