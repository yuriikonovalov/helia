package com.yuriikonovalov.helia.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.window.DialogProperties
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme


@Composable
fun Dialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = HeliaTheme.shapes.medium,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit
) {
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        BackgroundSurface(modifier = modifier.clip(shape)) {
            Box(modifier = Modifier) {
                content()
            }
        }
    }
}