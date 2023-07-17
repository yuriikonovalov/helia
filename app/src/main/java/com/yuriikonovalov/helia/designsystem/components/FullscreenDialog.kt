package com.yuriikonovalov.helia.designsystem.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider

// Window utils
@Composable
fun getDialogWindow(): Window? = (LocalView.current.parent as? DialogWindowProvider)?.window

@Composable
fun getActivityWindow(): Window? = LocalView.current.context.getActivityWindow()

private tailrec fun Context.getActivityWindow(): Window? =
    when (this) {
        is Activity -> window
        is ContextWrapper -> baseContext.getActivityWindow()
        else -> null
    }

@Composable
fun FullScreenDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(decorFitsSystemWindows = false)
    ) {
        BackgroundSurface(modifier = modifier.fillMaxSize()) {
            val activityWindow = getActivityWindow()
            val dialogWindow = getDialogWindow()
            val parentView = LocalView.current.parent as View
            SideEffect {
                if (activityWindow != null && dialogWindow != null) {
                    val attributes = WindowManager.LayoutParams()
                    attributes.copyFrom(activityWindow.attributes)
                    attributes.type = dialogWindow.attributes.type
                    dialogWindow.attributes = attributes
                    parentView.layoutParams = FrameLayout.LayoutParams(
                        activityWindow.decorView.width,
                        activityWindow.decorView.height
                    )
                }
            }
            content()
        }
    }
}