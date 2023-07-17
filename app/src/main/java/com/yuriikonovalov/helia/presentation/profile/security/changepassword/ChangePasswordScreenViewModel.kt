package com.yuriikonovalov.helia.presentation.profile.security.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriikonovalov.helia.domain.usecases.ChangePasswordResult
import com.yuriikonovalov.helia.domain.usecases.ChangePasswordUseCase
import com.yuriikonovalov.helia.domain.usecases.SignOutUseCase
import com.yuriikonovalov.helia.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordScreenViewModel @Inject constructor(
    private val changePassword: ChangePasswordUseCase,
    private val signOut: SignOutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ChangePasswordUiState())
    val state = _state.asStateFlow()

    fun handleIntent(intent: ChangePasswordIntent) = when (intent) {
        ChangePasswordIntent.Change -> handleChange()
        is ChangePasswordIntent.InputCurrentPassword -> _state.update {
            it.updateCurrentPassword(intent.password)
        }

        is ChangePasswordIntent.InputNewPassword -> _state.update {
            it.updateNewPassword(intent.password)
        }

        ChangePasswordIntent.ErrorShown -> _state.update { it.updateIsError(false) }
        ChangePasswordIntent.RecentLoginRequiredDialogDismissed -> _state.update {
            it.updateIsRecentLoginRequiredDialogOpen(false)
        }

        ChangePasswordIntent.SignOut -> handleSignOut()
    }

    private fun handleSignOut() {
        viewModelScope.launch {
            signOut()
        }
    }


    private fun handleChange() {
        viewModelScope.launch {
            _state.update { it.updateIsLoading(true) }
            val result = changePassword(
                currentPassword = state.value.currentPassword,
                newPassword = state.value.newPassword
            )
            _state.update { it.updateIsLoading(false) }
            when (result) {
                is Result.Success -> {
                    when (result.data) {
                        ChangePasswordResult.RecentLoginRequired -> _state.update {
                            it.updateIsRecentLoginRequiredDialogOpen(true)
                        }

                        ChangePasswordResult.Success -> _state.update { it.updateIsChanged(true) }
                        ChangePasswordResult.WeakPassword -> _state.update {
                            it.updateIsWeakPassword(true)
                        }

                        ChangePasswordResult.WrongPassword -> _state.update {
                            it.updateIsWrongPassword(true)
                        }
                    }
                }

                is Result.Error -> _state.update { it.updateIsError(true) }
            }
        }
    }
}