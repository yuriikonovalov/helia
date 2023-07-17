package com.yuriikonovalov.helia.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme

@Composable
fun SearchBar(
    value: String,
    onValueChanged: (String) -> Unit,
    onSearchKeyboardAction: () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onFilterClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val unfocusedLeadingIconColor =
        if (HeliaTheme.theme.isDark) HeliaTheme.colors.greyscale600 else HeliaTheme.colors.greyscale400
    val unfocusedTrailingIconColor = HeliaTheme.colors.primary500

    InputField(
        modifier = modifier.then(
            if (onClick != null)
                Modifier
                    .clip(InputFieldDefaults.shape)
                    .clickable(onClick = onClick)
            else Modifier
        ),
        enabled = onClick == null,
        value = value,
        onValueChange = onValueChanged,
        placeholderText = stringResource(R.string.search_bar_placeholder),
        unfocusedLeadingIconColor = unfocusedLeadingIconColor,
        unfocusedTrailingIconColor = unfocusedTrailingIconColor,
        leadingIcon = {
            Icon(
                modifier = Modifier.size(InputFieldDefaults.iconSize),
                painter = painterResource(id = R.drawable.ic_search_border),
                contentDescription = null
            )
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .size(InputFieldDefaults.iconSize)
                    .then(
                        if (onFilterClick != null)
                            Modifier
                                .clip(HeliaTheme.shapes.small)
                                .clickable(onClick = onFilterClick)
                        else
                            Modifier
                    ),
                painter = painterResource(id = R.drawable.ic_filter_border),
                contentDescription = stringResource(R.string.cd_filter_icon)
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchKeyboardAction() }),
        interactionSource = interactionSource
    )
}