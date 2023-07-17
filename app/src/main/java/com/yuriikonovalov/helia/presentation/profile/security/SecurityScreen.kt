package com.yuriikonovalov.helia.presentation.profile.security

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.components.Dialog
import com.yuriikonovalov.helia.designsystem.components.Divider
import com.yuriikonovalov.helia.designsystem.components.LoadingDialog
import com.yuriikonovalov.helia.designsystem.components.Navbar
import com.yuriikonovalov.helia.designsystem.components.PrimaryButton
import com.yuriikonovalov.helia.designsystem.components.SecondaryButton
import com.yuriikonovalov.helia.designsystem.components.Toggle
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme

@Composable
fun SecurityScreen(
    onNavigateClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    viewModel: SecurityScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SecurityScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        state = state,
        onNavigateClick = onNavigateClick,
        onChangePasswordClick = onChangePasswordClick,
        onRememberMeChanged = { viewModel.handleIntent(SecurityIntent.ToggleRememberMe(it)) },
        onDeleteProfileClick = { viewModel.handleIntent(SecurityIntent.DeleteProfile) },
        onSignOut = { viewModel.handleIntent(SecurityIntent.SignOut) },
        onRecentLoginRequiredDialogDismissed = { viewModel.handleIntent(SecurityIntent.DismissRecentLoginRequiredDialog) }
    )
}

@Composable
private fun SecurityScreenContent(
    state: SecurityUiState,
    onNavigateClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onRememberMeChanged: (Boolean) -> Unit,
    onDeleteProfileClick: () -> Unit,
    onSignOut: () -> Unit,
    onRecentLoginRequiredDialogDismissed: () -> Unit,
    modifier: Modifier = Modifier
) {

    var isDeleteProfileDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }

    LoadingDialog(open = state.isLoading)
    Column(modifier = modifier) {
        Navbar(
            modifier = Modifier
                .statusBarsPadding()
                .padding(vertical = 16.dp),
            title = stringResource(R.string.security_screen_title),
            onNavigateClick = onNavigateClick,
            actions = {}
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            RememberMeToggle(
                modifier = Modifier.fillMaxWidth(),
                checked = state.rememberMe,
                onCheckedChanged = onRememberMeChanged
            )
            if (state.isChangePasswordAvailable) {
                Spacer(modifier = Modifier.height(24.dp))
                SecondaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.security_screen_change_password_button),
                    onClick = onChangePasswordClick
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            SecondaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.security_screen_delete_profile_button),
                onClick = { isDeleteProfileDialogOpen = true }
            )
        }
    }

    DeleteProfileDialog(
        open = isDeleteProfileDialogOpen,
        onDelete = {
            onDeleteProfileClick()
            isDeleteProfileDialogOpen = false
        },
        onDismiss = { isDeleteProfileDialogOpen = false }
    )
    RecentLoginRequiredDialog(
        open = state.isRecentLoginRequiredDialogOpen,
        title = stringResource(R.string.recent_login_required_dialog_heading),
        body = stringResource(R.string.recent_login_required_dialog_body),
        positiveButtonText = stringResource(R.string.recent_login_required_dialog_positive_button),
        negativeButtonText = stringResource(R.string.cancel),
        onPositiveButtonClick = onSignOut,
        onDismiss = onRecentLoginRequiredDialogDismissed
    )
}


@Composable
private fun DeleteProfileDialog(
    open: Boolean,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    if (open) {
        Dialog(onDismissRequest = onDismiss) {
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.delete_profile_dialog_heading),
                    color = HeliaTheme.colors.error,
                    style = HeliaTheme.typography.heading4
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                )

                Text(
                    text = stringResource(R.string.delete_profile_dialog_body),
                    style = HeliaTheme.typography.heading6,
                    color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale800,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.delete_profile_dialog_positive_button),
                    onClick = onDelete
                )
                Spacer(modifier = Modifier.height(16.dp))
                SecondaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.cancel),
                    onClick = onDismiss
                )

            }
        }
    }
}

@Composable
private fun RememberMeToggle(
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.security_screen_remember_me_toggle),
            style = HeliaTheme.typography.bodyXLargeSemiBold,
            color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale800
        )
        Toggle(checked = checked, onCheckedChange = onCheckedChanged)
    }
}
