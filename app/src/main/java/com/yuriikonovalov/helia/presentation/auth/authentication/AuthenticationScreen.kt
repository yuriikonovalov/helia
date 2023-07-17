package com.yuriikonovalov.helia.presentation.auth.authentication

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.components.AuthenticationHeading
import com.yuriikonovalov.helia.designsystem.components.Divider
import com.yuriikonovalov.helia.designsystem.components.LoadingDialog
import com.yuriikonovalov.helia.designsystem.components.LoginGoogleButton
import com.yuriikonovalov.helia.designsystem.components.PrimaryButton
import com.yuriikonovalov.helia.designsystem.components.TextButton
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme
import com.yuriikonovalov.helia.presentation.auth.GoogleAuthUiClient
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map


@Composable
fun AuthenticationScreen(
    onSignUn: () -> Unit,
    onSignInWithPassword: () -> Unit,
    onNavigateToFillProfile: () -> Unit,
    viewModel: AuthenticationScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val googleAuthUiClient = remember { GoogleAuthUiClient(context) }
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val token = googleAuthUiClient.getToken(result.data!!)
                viewModel.handleIntent(AuthenticationIntent.SignInWithGoogleToken(token))
            } else {
                viewModel.handleIntent(AuthenticationIntent.CancelSigningInWithGoogle)
            }
        }
    )

    LoadingDialog(open = state.loading)

    AuthenticationScreenContent(
        modifier = Modifier.navigationBarsPadding(),
        onSignUn = onSignUn,
        onContinueWithGoogleClick = { viewModel.handleIntent(AuthenticationIntent.LaunchSigningInWithGoogle) },
        onSignInWithPasswordClick = onSignInWithPassword
    )

    LaunchedEffect(state) {
        viewModel.state.map { it.signInWithGoogleError }
            .filter { it }
            .collect {
                Toast.makeText(
                    context,
                    context.getString(R.string.authentication_something_went_wrong),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.handleIntent(AuthenticationIntent.SignInWithGoogleErrorShown)
            }
    }

    if (state.signedUpNewUser) {
        LaunchedEffect(Unit) {
            onNavigateToFillProfile()
        }
    }

    if (state.signInWithGoogle) {
        LaunchedEffect(Unit) {
            googleLauncher.launch(
                googleAuthUiClient.getIntentSenderRequest() ?: return@LaunchedEffect
            )
            viewModel.handleIntent(AuthenticationIntent.SigningInWithGoogleLaunched)
        }
    }

}

@Composable
private fun AuthenticationScreenContent(
    onSignUn: () -> Unit,
    onContinueWithGoogleClick: () -> Unit,
    onSignInWithPasswordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            AuthenticationHeading(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.authentication_heading),
                textAlign = TextAlign.Center
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                LoginGoogleButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onContinueWithGoogleClick
                )
                Spacer(modifier = Modifier.height(40.dp))
                Divider(modifier = Modifier.fillMaxWidth(), text = stringResource(R.string.or))
                Spacer(modifier = Modifier.height(40.dp))
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.sign_in_with_password),
                    onClick = onSignInWithPasswordClick
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.don_t_have_an_account),
                style = HeliaTheme.typography.bodyMediumRegular,
                color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.greyscale100 else HeliaTheme.colors.greyscale500
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextButton(
                text = stringResource(R.string.sign_up), onClick = onSignUn
            )
        }
    }
}

