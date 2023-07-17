package com.yuriikonovalov.helia.presentation.auth.signup

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.components.AuthenticationHeading
import com.yuriikonovalov.helia.designsystem.components.Checkbox
import com.yuriikonovalov.helia.designsystem.components.Dialog
import com.yuriikonovalov.helia.designsystem.components.EmailInputField
import com.yuriikonovalov.helia.designsystem.components.LoadingDialog
import com.yuriikonovalov.helia.designsystem.components.Navbar
import com.yuriikonovalov.helia.designsystem.components.PasswordInputField
import com.yuriikonovalov.helia.designsystem.components.PrimaryButton
import com.yuriikonovalov.helia.designsystem.components.SecondaryButton
import com.yuriikonovalov.helia.designsystem.components.TextButton
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme
import com.yuriikonovalov.helia.presentation.auth.GoogleAuthUiClient
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Composable
fun SignUpScreen(
    onNavigateClick: () -> Unit,
    onSignIn: () -> Unit,
    onFillProfile: () -> Unit,
    viewModel: SignUpScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val googleAuthUiClient = remember { GoogleAuthUiClient(context) }
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val token = googleAuthUiClient.getToken(result.data!!)
                viewModel.handleIntent(SignUpIntent.SignInWithGoogleToken(token))
            } else {
                viewModel.handleIntent(SignUpIntent.CancelSigningInWithGoogle)
            }
        }
    )

    SignUpScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .statusBarsPadding(),
        state = state,
        onNavigateClick = onNavigateClick,
        onSignIn = onSignIn,
        onSignUp = { viewModel.handleIntent(SignUpIntent.SignUp) },
        onEmailChanged = { viewModel.handleIntent(SignUpIntent.InputEmail(it)) },
        onPasswordChanged = { viewModel.handleIntent(SignUpIntent.InputPassword(it)) },
        onRememberMeChanged = { viewModel.handleIntent(SignUpIntent.CheckRememberMe) },
        onAccountExistDialogCancel = { viewModel.handleIntent(SignUpIntent.CloseAccountAlreadyExistDialog) },
        onSignInWithGoogle = { viewModel.handleIntent(SignUpIntent.LaunchSigningInWithGoogle) },
        onEmailAlreadyInUseWithGoogleDialogCancel = { viewModel.handleIntent(SignUpIntent.CloseEmailAlreadyInUseWithGoogleDialog) }
    )

    LaunchedEffect(state) {
        viewModel.state.map { it.signInError }
            .filter { it }
            .collect {
                Toast.makeText(
                    context,
                    context.getString(R.string.authentication_something_went_wrong),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.handleIntent(SignUpIntent.SignInErrorShown)
            }
    }

    if (state.userCreated) {
        LaunchedEffect(Unit) {
            onFillProfile()
        }
    }

    if (state.signInWithGoogle) {
        LaunchedEffect(Unit) {
            googleLauncher.launch(
                googleAuthUiClient.getIntentSenderRequest() ?: return@LaunchedEffect
            )
            viewModel.handleIntent(SignUpIntent.SigningInWithGoogleLaunched)
        }
    }
}

@Composable
private fun SignUpScreenContent(
    state: SignUpUiState,
    onNavigateClick: () -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRememberMeChanged: (Boolean) -> Unit,
    onSignUp: () -> Unit,
    onSignIn: () -> Unit,
    onAccountExistDialogCancel: () -> Unit,
    onSignInWithGoogle: () -> Unit,
    onEmailAlreadyInUseWithGoogleDialogCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    LoadingDialog(open = state.isLoading)
    AccountExistDialog(
        open = state.isEmailAlreadyInUseDialogOpen,
        onNavigateToSignIn = onSignIn,
        onCancel = onAccountExistDialogCancel
    )
    EmailAlreadyInUseWithGoogleDialog(
        open = state.isEmailAlreadyInUseWithGoogleDialogOpen,
        onSignInWithGoogle = onSignInWithGoogle,
        onCancel = onEmailAlreadyInUseWithGoogleDialogCancel
    )

    Column(
        modifier = modifier.padding(bottom = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Navbar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            title = "",
            onNavigateClick = onNavigateClick,
            actions = {}
        )
        AuthenticationHeading(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            text = stringResource(R.string.sign_up_screen_heading)
        )

        Spacer(modifier = Modifier.height(40.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmailInputField(
                modifier = Modifier.fillMaxWidth(),
                value = state.email,
                onValueChange = onEmailChanged,
                isError = !state.isValidEmail,
                supportingText = if (!state.isValidEmail) "Invalid email" else null,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(24.dp))
            PasswordInputField(
                modifier = Modifier.fillMaxWidth(),
                value = state.password,
                onValueChange = onPasswordChanged,
                isError = !state.isValidPassword,
                supportingText = if (!state.isValidPassword) "Invalid password" else null
            )
            Spacer(modifier = Modifier.height(24.dp))
            Checkbox(
                text = stringResource(R.string.remember_me),
                checked = state.rememberMe,
                onCheckedChange = onRememberMeChanged
            )
            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.sign_up),
                onClick = onSignUp,
                enabled = state.isSignUpButtonEnabled
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.already_have_an_account),
                style = HeliaTheme.typography.bodyMediumRegular,
                color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.greyscale100 else HeliaTheme.colors.greyscale500
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextButton(
                text = stringResource(R.string.sign_in), onClick = onSignIn
            )
        }
    }
}

@Composable
private fun AccountExistDialog(
    open: Boolean,
    onNavigateToSignIn: () -> Unit,
    onCancel: () -> Unit
) {
    if (open) {
        Dialog(
            onDismissRequest = onCancel,
            shape = HeliaTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.sign_up_account_exist_dialog_heading),
                    style = HeliaTheme.typography.heading4,
                    color = HeliaTheme.colors.primary500,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.sign_up_account_exist_dialog_body),
                    style = HeliaTheme.typography.bodyLargeRegular,
                    color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.sign_up_account_exist_dialog_primary_button),
                    onClick = onNavigateToSignIn
                )
                Spacer(modifier = Modifier.height(16.dp))
                SecondaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.cancel),
                    onClick = onCancel
                )
            }
        }
    }
}


@Composable
private fun EmailAlreadyInUseWithGoogleDialog(
    open: Boolean,
    onSignInWithGoogle: () -> Unit,
    onCancel: () -> Unit
) {
    if (open) {
        Dialog(
            onDismissRequest = onCancel,
            shape = HeliaTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.sign_up_email_with_google_dialog_heading),
                    style = HeliaTheme.typography.heading4,
                    color = HeliaTheme.colors.primary500,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.sign_up_email_with_google_dialog_body),
                    style = HeliaTheme.typography.bodyLargeRegular,
                    color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.login_google_button_text),
                    onClick = onSignInWithGoogle
                )
                Spacer(modifier = Modifier.height(16.dp))
                SecondaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.cancel),
                    onClick = onCancel
                )
            }
        }
    }
}