package com.yuriikonovalov.helia.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.clickableWithoutIndication
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme

object InputFieldDefaults {
    val iconSize = 20.dp

    val shape
        @Composable get() = HeliaTheme.shapes.small

    @Composable
    fun textColor() =
        if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900

    @Composable
    fun placeholderColor() = HeliaTheme.colors.greyscale500

    @Composable
    fun focusedBorderColor() = HeliaTheme.colors.primary500

    @Composable
    fun unfocusedBorderColor() = Color.Transparent

    @Composable
    fun focusedContainerColor() = Color(0x141AB65C)

    @Composable
    fun unfocusedContainerColor() =
        if (HeliaTheme.theme.isDark) HeliaTheme.colors.dark2 else HeliaTheme.colors.greyscale50


    @Composable
    fun unfocusedIconColor(customColor: Color, isPlaceholder: Boolean) = customColor.takeOrElse {
        return if (HeliaTheme.theme.isDark) {
            if (isPlaceholder) HeliaTheme.colors.greyscale500 else HeliaTheme.colors.white
        } else {
            if (isPlaceholder) HeliaTheme.colors.greyscale500 else HeliaTheme.colors.greyscale900
        }
    }

    @Composable
    fun focusedIconColor() = HeliaTheme.colors.primary500

    @Composable
    fun cursorColor() = HeliaTheme.colors.primary500

    @Composable
    fun selectionColors() = TextSelectionColors(
        handleColor = HeliaTheme.colors.primary500,
        backgroundColor = HeliaTheme.colors.primary300
    )

    @Composable
    fun errorColor() = HeliaTheme.colors.error
}


@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    textStyle: TextStyle = HeliaTheme.typography.bodyMediumSemiBold,
    unfocusedLeadingIconColor: Color = Color.Unspecified,
    unfocusedTrailingIconColor: Color = Color.Unspecified,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {

    val isPlaceholder by remember(value.isEmpty()) { mutableStateOf(value.isEmpty()) }

    val colors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = InputFieldDefaults.textColor(),
        unfocusedTextColor = InputFieldDefaults.textColor(),
        unfocusedPlaceholderColor = InputFieldDefaults.placeholderColor(),
        focusedPlaceholderColor = InputFieldDefaults.placeholderColor(),
        focusedBorderColor = InputFieldDefaults.focusedBorderColor(),
        unfocusedBorderColor = InputFieldDefaults.unfocusedBorderColor(),
        focusedContainerColor = InputFieldDefaults.focusedContainerColor(),
        // Passing an unfocused container color in this way cases some blinking in the transition
        // between focused and unfocused states.
        // unfocusedContainerColor = InputFieldDefaults.unfocusedContainerColor(),
        unfocusedLeadingIconColor = InputFieldDefaults.unfocusedIconColor(
            customColor = unfocusedLeadingIconColor,
            isPlaceholder = isPlaceholder
        ),
        focusedLeadingIconColor = InputFieldDefaults.focusedIconColor(),
        unfocusedTrailingIconColor = InputFieldDefaults.unfocusedIconColor(
            customColor = unfocusedTrailingIconColor,
            isPlaceholder = isPlaceholder
        ),
        focusedTrailingIconColor = InputFieldDefaults.focusedIconColor(),
        cursorColor = InputFieldDefaults.cursorColor(),
        selectionColors = InputFieldDefaults.selectionColors(),
        errorBorderColor = InputFieldDefaults.errorColor(),
        errorCursorColor = InputFieldDefaults.errorColor(),
        errorLeadingIconColor = InputFieldDefaults.errorColor(),
        errorTrailingIconColor = InputFieldDefaults.errorColor(),
        errorSupportingTextColor = InputFieldDefaults.errorColor(),
        disabledBorderColor = Color.Transparent,
        disabledTrailingIconColor = InputFieldDefaults.unfocusedIconColor(
            customColor = unfocusedTrailingIconColor,
            isPlaceholder = isPlaceholder
        ),
        disabledLeadingIconColor = InputFieldDefaults.unfocusedIconColor(
            customColor = unfocusedLeadingIconColor,
            isPlaceholder = isPlaceholder
        ),
        disabledPlaceholderColor = InputFieldDefaults.placeholderColor(),
        disabledTextColor = InputFieldDefaults.textColor()
    )

//    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    OutlinedTextField(
        modifier = modifier.then(
            // A workaround of some blinking in the transition between focused and unfocused states.
            if (!focused) Modifier.background(
                color = InputFieldDefaults.unfocusedContainerColor(),
                shape = InputFieldDefaults.shape
            ) else Modifier
        ),
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        enabled = enabled,
        singleLine = true,
        shape = InputFieldDefaults.shape,
        colors = colors,
        placeholder = {
            Text(
                text = placeholderText,
                style = HeliaTheme.typography.bodyMediumRegular
            )
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        supportingText = supportingText?.let { { Text(text = it) } },
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource
    )
}


@Composable
fun UsernameInputField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier,
    supportingText: String? = null
) {
    InputField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholderText = stringResource(R.string.username_input_field_placeholder),
        supportingText = supportingText,
        isError = isError,
        leadingIcon = {
            Icon(
                modifier = Modifier.size(InputFieldDefaults.iconSize),
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = stringResource(R.string.cd_profile_icon)
            )
        },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
    )
}

@Composable
fun PasswordInputField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    placeholderText: String = stringResource(R.string.passwrord_input_field_placeholder),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        autoCorrect = false,
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done
    )
) {
    var passwordHidden by remember { mutableStateOf(true) }

    InputField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholderText = placeholderText,
        supportingText = supportingText,
        isError = isError,
        leadingIcon = {
            Icon(
                modifier = Modifier.size(InputFieldDefaults.iconSize),
                painter = painterResource(id = R.drawable.ic_lock),
                contentDescription = stringResource(R.string.cd_password_icon)
            )
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .size(InputFieldDefaults.iconSize)
                    .clickableWithoutIndication { passwordHidden = !passwordHidden },
                painter = painterResource(id = if (passwordHidden) R.drawable.ic_hide else R.drawable.ic_show),
                contentDescription = stringResource(R.string.cd_password_icon)
            )
        },
        visualTransformation = if (passwordHidden) PasswordVisualTransformation('●') else VisualTransformation.None,
        keyboardOptions = keyboardOptions
    )
}

@Composable
fun EmailInputField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    InputField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholderText = stringResource(R.string.email_input_field_placeholder),
        supportingText = supportingText,
        isError = isError,
        leadingIcon = {
            Icon(
                modifier = Modifier.size(InputFieldDefaults.iconSize),
                painter = painterResource(id = R.drawable.ic_message),
                contentDescription = stringResource(R.string.cd_email_icon)
            )
        },
        keyboardOptions = keyboardOptions
    )
}