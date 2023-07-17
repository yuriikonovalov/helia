package com.yuriikonovalov.helia.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yuriikonovalov.helia.designsystem.components.BackgroundSurface
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme
import com.yuriikonovalov.helia.domain.valueobjects.Theme
import com.yuriikonovalov.helia.presentation.auth.authentication.AuthenticationNavigation
import com.yuriikonovalov.helia.presentation.home.HomeNavigation
import com.yuriikonovalov.helia.presentation.navigation.HeliaNavigationWrapper
import com.yuriikonovalov.helia.presentation.welcome.WelcomeNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        val userLoggedIn = auth.currentUser != null
        viewModel.handleIntent(MainActivityIntent.AuthStateChange(userLoggedIn))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        Firebase.auth.addAuthStateListener(authStateListener)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        var state: MainActivityUiState by mutableStateOf(MainActivityUiState())

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    state = it
                }
            }
        }

        splashScreen.setKeepOnScreenCondition { state.isLoading }

        setContent {
            val systemUiController = rememberSystemUiController()
            val darkTheme = isDarkTheme(state)
            // Update the dark content of the system bars to match the theme.
            DisposableEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                onDispose { }
            }

            HeliaTheme(darkTheme = darkTheme) {
                BackgroundSurface(modifier = Modifier.fillMaxSize()) {
                    HeliaNavigationWrapper(
                        modifier = Modifier.fillMaxSize(),
                        startDestination = state.startDestination()
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        Firebase.auth.removeAuthStateListener(authStateListener)
        super.onDestroy()
    }

    private fun MainActivityUiState.startDestination(): String = if (showOnboarding) {
        WelcomeNavigation.route
    } else if (userLoggedIn) {
        HomeNavigation.route
    } else {
        AuthenticationNavigation.route
    }

    @Composable
    private fun isDarkTheme(state: MainActivityUiState) = when (state.theme) {
        Theme.SYSTEM -> isSystemInDarkTheme()
        Theme.LIGHT -> false
        Theme.DARK -> true
    }
}
