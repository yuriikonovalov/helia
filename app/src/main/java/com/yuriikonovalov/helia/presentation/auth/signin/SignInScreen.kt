package com.yuriikonovalov.helia.presentation.auth.signin

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
fun SignInScreen(
    onNavigateClick: () -> Unit,
    onSignUp: () -> Unit,
    onForgotPassword: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SignInScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val googleAuthUiClient = remember { GoogleAuthUiClient(context) }
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val token = googleAuthUiClient.getToken(result.data!!)
                viewModel.handleIntent(SignInIntent.SignInWithGoogleToken(token))
            } else {
                viewModel.handleIntent(SignInIntent.CancelSigningInWithGoogle)
            }
        }
    )

    LoadingDialog(open = state.isLoading)

    EmailAssociatedWithGoogleDialog(
        open = state.emailAssociatedWithGoogle,
        heading = stringResource(R.string.sign_up_email_with_google_dialog_heading),
        onSignInWithGoogle = { viewModel.handleIntent(SignInIntent.LaunchSigningInWithGoogle) },
        onCancel = { viewModel.handleIntent(SignInIntent.EmailAssociatedWithGoogleShown) }
    )

    EmailAssociatedWithGoogleDialog(
        open = state.wrongPasswordAndEmailAssociatedWithGoogle,
        heading = stringResource(R.string.sign_in_screen_wrong_password_dialog_heading),
        onSignInWithGoogle = { viewModel.handleIntent(SignInIntent.LaunchSigningInWithGoogle) },
        onCancel = { viewModel.handleIntent(SignInIntent.WrongPasswordAndEmailAssociatedWithGoogleShown) }
    )

    SignInScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .statusBarsPadding(),
        state = state,
        onNavigateClick = onNavigateClick,
        onSignUp = onSignUp,
        onForgotPassword = onForgotPassword,
        onSignIn = { viewModel.handleIntent(SignInIntent.SignIn) },
        onEmailChanged = { viewModel.handleIntent(SignInIntent.InputEmail(it)) },
        onPasswordChanged = { viewModel.handleIntent(SignInIntent.InputPassword(it)) },
        onRememberMeChanged = { viewModel.handleIntent(SignInIntent.CheckRememberMe) }
    )

    if (state.isSignInSuccessful) {
        LaunchedEffect(Unit) {
            onNavigateToHome()
        }
    }

    if (state.signInWithGoogle) {
        LaunchedEffect(Unit) {
            googleLauncher.launch(
                googleAuthUiClient.getIntentSenderRequest() ?: return@LaunchedEffect
            )
            viewModel.handleIntent(SignInIntent.SigningInWithGoogleLaunched)
        }
    }

    LaunchedEffect(state.signInError) {
        viewModel.state.map { it.signInError }
            .filter { it }
            .collect {
                Toast.makeText(
                    context,
                    context.getString(R.string.sign_in_screen_sign_in_error),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.handleIntent(SignInIntent.SignInErrorShown)
            }
    }

    LaunchedEffect(state.wrongPassword) {
        viewModel.state.map { it.wrongPassword }
            .filter { it }
            .collect {
                Toast.makeText(
                    context,
                    context.getText(R.string.sign_in_screen_wrong_password),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.handleIntent(SignInIntent.WrongPasswordErrorShown)
            }
    }

    LaunchedEffect(state.userNotFound) {
        viewModel.state.map { it.userNotFound }
            .filter { it }
            .collect {
                Toast.makeText(
                    context,
                    context.getString(R.string.sign_in_screen_user_not_found),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.handleIntent(SignInIntent.UserNotFoundErrorShown)
            }
    }
}

@Composable
private fun SignInScreenContent(
    state: SignInUiState,
    onNavigateClick: () -> Unit,
    onSignUp: () -> Unit,
    onForgotPassword: () -> Unit,
    modifier: Modifier = Modifier,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRememberMeChanged: (Boolean) -> Unit,
    onSignIn: () -> Unit
) {
    Column(
        modifier = modifier.padding(bottom = 24.dp)
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
            text = stringResource(R.string.sign_in_screen_heading)
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
                supportingText = if (!state.isValidEmail) stringResource(R.string.sign_in_screen_invalid_email) else null
            )
            Spacer(modifier = Modifier.height(24.dp))
            PasswordInputField(
                modifier = Modifier.fillMaxWidth(),
                value = state.password,
                onValueChange = onPasswordChanged,
                isError = false,
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
                text = stringResource(R.string.sign_in),
                onClick = onSignIn,
                enabled = state.isSignInButtonEnabled
            )
            Spacer(modifier = Modifier.height(24.dp))
            TextButton(
                text = stringResource(R.string.forgot_the_password),
                onClick = onForgotPassword
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
                text = stringResource(R.string.don_t_have_an_account),
                style = HeliaTheme.typography.bodyMediumRegular,
                color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.greyscale100 else HeliaTheme.colors.greyscale500
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextButton(
                text = stringResource(R.string.sign_up), onClick = onSignUp
            )
        }
    }
}


@Composable
private fun EmailAssociatedWithGoogleDialog(
    open: Boolean,
    heading: String,
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
                    text = heading,
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