package com.yuriikonovalov.helia.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme

private object CheckboxDefaults {
    val size = 24.dp
    val shape = RoundedCornerShape(8.dp)

    @Composable
    fun checkedColor() = HeliaTheme.colors.primary500

    @Composable
    fun uncheckedColor() = HeliaTheme.colors.primary500

    @Composable
    fun checkmarkColor() = HeliaTheme.colors.white

    @Composable
    fun textColor() =
        if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900
}

@Composable
fun Checkbox(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CheckboxDefaults.shape)
                .size(CheckboxDefaults.size)
                .border(3.dp, CheckboxDefaults.uncheckedColor(), CheckboxDefaults.shape)
                .then(if (checked) Modifier.background(CheckboxDefaults.checkedColor()) else Modifier)
                .toggleable(
                    value = checked,
                    role = Role.Checkbox,
                    onValueChange = onCheckedChange
                ),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(
                    modifier = Modifier.size(width = 13.dp, height = 10.dp),
                    painter = painterResource(id = R.drawable.ic_checkmark),
                    contentDescription = null,
                    tint = CheckboxDefaults.checkmarkColor()
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = HeliaTheme.typography.bodyMediumSemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = CheckboxDefaults.textColor()
        )
    }
}