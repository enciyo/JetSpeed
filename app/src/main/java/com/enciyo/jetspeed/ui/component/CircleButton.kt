package com.enciyo.jetspeed.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

//val onText = text.ifEmpty { stringResource(id = R.string.go) }
//if (text.isEmpty()) MaterialTheme.typography.displayLarge else MaterialTheme.typography.titleMedium

@Composable
fun CircleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    style: TextStyle
) {
    Text(
        text = text,
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
