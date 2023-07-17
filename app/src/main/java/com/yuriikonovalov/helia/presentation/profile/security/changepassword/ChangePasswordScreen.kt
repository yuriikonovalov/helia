package com.yuriikonovalov.helia.presentation.profile.security.changepassword

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.components.LoadingDialog
import com.yuriikonovalov.helia.designsystem.components.Navbar
import com.yuriikonovalov.helia.designsystem.components.PasswordInputField
import com.yuriikonovalov.helia.designsystem.components.PrimaryButton
import com.yuriikonovalov.helia.presentation.profile.security.RecentLoginRequiredDialog

@Composable
fun ChangePasswordScreen(
    onNavigateUp: () -> Unit,
    viewModel: ChangePasswordScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ChangePasswordScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        state = state,
        onNavigateClick = onNavigateUp,
        onChangeClick = { viewModel.handleIntent(ChangePasswordIntent.Change) },
        onCurrentPasswordChanged = {
            viewModel.handleIntent(ChangePasswordIntent.InputCurrentPassword(it))
        },
        onNewPasswordChanged = {
            viewModel.handleIntent(ChangePasswordIntent.InputNewPassword(it))
        }
    )

    RecentLoginRequiredDialog(
        open = state.isRecentLoginRequiredDialogOpen,
        title = stringResource(R.string.recent_login_required_dialog_heading),
        body = stringResource(R.string.change_password_screen_recent_login_required_dialog_body),
        positiveButtonText = stringResource(R.string.recent_login_required_dialog_positive_button),
        negativeButtonText = stringResource(R.string.cancel),
        onPositiveButtonClick = { viewModel.handleIntent(ChangePasswordIntent.SignOut) },
        onDismiss = { viewModel.handleIntent(ChangePasswordIntent.RecentLoginRequiredDialogDismissed) }
    )

    if (state.isError) {
        Toast.makeText(
            context,
            stringResource(R.string.change_password_screen_error),
            Toast.LENGTH_SHORT
        ).show()
        viewModel.handleIntent(ChangePasswordIntent.ErrorShown)
    }

    if (state.isChanged) {
        LaunchedEffect(Unit) {
            onNavigateUp()
        }
    }
}

@Composable
private fun ChangePasswordScreenContent(
    state: ChangePasswordUiState,
    onNavigateClick: () -> Unit,
    onChangeClick: () -> Unit,
    onCurrentPasswordChanged: (String) -> Unit,
    onNewPasswordChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LoadingDialog(open = state.isLoading)

    Column(modifier = modifier) {
        Navbar(
            modifier = Modifier
                .statusBarsPadding()
                .padding(vertical = 16.dp),
            title = stringResource(R.string.change_password_screen_title),
            onNavigateClick = onNavigateClick,
            actions = {}
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {

            PasswordInputField(
                modifier = Modifier.fillMaxWidth(),
                value = state.currentPassword,
                onValueChange = onCurrentPasswordChanged,
                isError = state.isWrongPassword,
                placeholderText = stringResource(R.string.change_password_screen_current_password_placeholder),
                supportingText = if (state.isWrongPassword) {
                    stringResource(R.string.change_password_screen_current_password_error)
                } else {
                    null
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(24.dp))
            PasswordInputField(
                modifier = Modifier.fillMaxWidth(),
                value = state.newPassword,
                onValueChange = onNewPasswordChanged,
                placeholderText = stringResource(R.string.change_password_screen_new_password_placeholder),
                isError = state.isWeakPassword,
                supportingText = if (state.isWeakPassword) {
                    stringResource(R.string.change_password_screen_new_password_error)
                } else {
                    null
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.change_password_screen_change_button),
                onClick = onChangeClick,
                enabled = state.isChangeButtonEnabled
            )
        }
    }
}