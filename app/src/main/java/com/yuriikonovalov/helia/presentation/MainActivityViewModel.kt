package com.yuriikonovalov.helia.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriikonovalov.helia.domain.usecases.GetAppPreferencesUseCase
import com.yuriikonovalov.helia.domain.usecases.SignOutUseCase
import com.yuriikonovalov.helia.domain.valueobjects.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getAppPreferences: GetAppPreferencesUseCase,
    private val signOut: SignOutUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MainActivityUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            signOutIfNotRemembered()
            collectAppPreferences()
        }
    }

    private suspend fun signOutIfNotRemembered() {
        val rememberMe = getAppPreferences().map { it.rememberMe }.first()
        if (!rememberMe) signOut()
    }

    private suspend fun collectAppPreferences() {
        getAppPreferences()
            .collect { appPreferences ->
                _state.update { oldState ->
                    oldState.copy(
                        isLoading = false,
                        theme = appPreferences.theme,
                        showOnboarding = appPreferences.showOnboarding
                    )
                }
            }
    }


    fun handleIntent(intent: MainActivityIntent) = when (intent) {
        is MainActivityIntent.AuthStateChange -> handleAuthStateChange(intent.isUserLoggedIn)
    }

    private fun handleAuthStateChange(userLoggedIn: Boolean) {
        _state.update { oldState ->
            oldState.copy(userLoggedIn = userLoggedIn)
        }
    }
}


data class MainActivityUiState(
    val isLoading: Boolean = true,
    val theme: Theme = Theme.LIGHT,
    val showOnboarding: Boolean = true,
    val userLoggedIn: Boolean = false
)

sealed interface MainActivityIntent {
    data class AuthStateChange(val isUserLoggedIn: Boolean) : MainActivityIntent
}