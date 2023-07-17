package com.yuriikonovalov.helia.presentation.profile.security

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriikonovalov.helia.domain.usecases.DeleteProfileResult
import com.yuriikonovalov.helia.domain.usecases.DeleteProfileUseCase
import com.yuriikonovalov.helia.domain.usecases.GetAppPreferencesUseCase
import com.yuriikonovalov.helia.domain.usecases.IsPasswordChangeAvailableUseCase
import com.yuriikonovalov.helia.domain.usecases.SignOutUseCase
import com.yuriikonovalov.helia.domain.usecases.UpdateRememberMeUseCase
import com.yuriikonovalov.helia.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SecurityScreenViewModel @Inject constructor(
    private val deleteProfile: DeleteProfileUseCase,
    private val updateRememberMe: UpdateRememberMeUseCase,
    private val appPreferences: GetAppPreferencesUseCase,
    private val signOut: SignOutUseCase,
    private val isPasswordChangeAvailable: IsPasswordChangeAvailableUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SecurityUiState())
    val state = _state.asStateFlow()

    init {
        getPasswordChangeAvailability()
        collectRememberMePreference()
    }

    fun handleIntent(intent: SecurityIntent) = when (intent) {
        SecurityIntent.ChangePassword -> _state.update { it.updateChangePassword(true) }
        SecurityIntent.DeleteProfile -> handleDeleteProfile()
        SecurityIntent.ChangePasswordHandled -> _state.update { it.updateChangePassword(false) }
        is SecurityIntent.ToggleRememberMe -> handleToggleRememberMe(intent.rememberMe)
        SecurityIntent.DismissRecentLoginRequiredDialog -> _state.update {
            it.updateIsRecentLoginRequiredDialogOpen(false)
        }

        SecurityIntent.SignOut -> handleSignOut()
    }

    private fun handleSignOut() {
        viewModelScope.launch {
            _state.update { it.updateIsLoading(true) }
            signOut()
            _state.update { it.updateIsLoading(false) }
        }
    }

    private fun handleToggleRememberMe(rememberMe: Boolean) {
        viewModelScope.launch {
            updateRememberMe(rememberMe)
        }
    }

    private fun handleDeleteProfile() {
        viewModelScope.launch {
            _state.update { it.updateIsLoading(true) }
            val result = deleteProfile()
            _state.update { it.updateIsLoading(false) }
            when (result) {
                is Result.Success -> {
                    when (result.data) {
                        DeleteProfileResult.RecentLoginRequired -> _state.update {
                            it.updateIsRecentLoginRequiredDialogOpen(true)
                        }

                        DeleteProfileResult.Success -> {}
                    }
                }

                is Result.Error -> {}
            }
        }
    }

    private fun collectRememberMePreference() {
        viewModelScope.launch {
            appPreferences()
                .map { it.rememberMe }
                .collect { rememberMe ->
                    _state.update { it.updateRememberMe(rememberMe) }
                }
        }
    }

    private fun getPasswordChangeAvailability() {
        viewModelScope.launch {
            _state.update { it.updateIsLoading(true) }
            val result = isPasswordChangeAvailable()
            _state.update { it.updateIsLoading(false) }
            when (result) {
                is Result.Success -> _state.update { it.updateIsChangePasswordAvailable(result.data) }
                is Result.Error -> _state.update { it.updateIsChangePasswordAvailable(false) }
            }
        }
    }
}