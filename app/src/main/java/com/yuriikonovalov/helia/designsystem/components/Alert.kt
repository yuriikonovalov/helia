package com.yuriikonovalov.helia.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme


enum class AlertType {
    SUCCESS, ERROR
}

private object AlertDefaults {

    val shape
        @Composable get() = HeliaTheme.shapes.extraSmall

    @Composable
    fun contentColor(alertType: AlertType) = when (alertType) {
        AlertType.SUCCESS -> HeliaTheme.colors.success
        AlertType.ERROR -> HeliaTheme.colors.error
    }

    @Composable
    fun containerColor(alertType: AlertType) = when (alertType) {
        AlertType.SUCCESS -> HeliaTheme.colors.success.copy(alpha = 0.2f)
        AlertType.ERROR -> HeliaTheme.colors.error.copy(alpha = 0.2f)
    }

    fun iconResId(alertType: AlertType) = when (alertType) {
        AlertType.SUCCESS -> R.drawable.ic_tick_square
        AlertType.ERROR -> R.drawable.ic_info_circle
    }

}

@Composable
fun Alert(
    text: String,
    type: AlertType,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(AlertDefaults.shape)
            .background(AlertDefaults.containerColor(alertType = type))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = AlertDefaults.iconResId(type)),
                contentDescription = null,
                tint = AlertDefaults.contentColor(type)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                style = HeliaTheme.typography.bodyXSmallRegular,
                color = AlertDefaults.contentColor(type)
            )
        }
    }
}