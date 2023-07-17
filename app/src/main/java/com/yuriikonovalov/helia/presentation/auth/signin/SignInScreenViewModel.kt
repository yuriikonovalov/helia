package com.yuriikonovalov.helia.presentation.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriikonovalov.helia.domain.usecases.SignInWithEmailAndPasswordResult
import com.yuriikonovalov.helia.domain.usecases.SignInWithEmailAndPasswordUseCase
import com.yuriikonovalov.helia.domain.usecases.SignInWithGoogleUseCase
import com.yuriikonovalov.helia.domain.valueobjects.Token
import com.yuriikonovalov.helia.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInScreenViewModel @Inject constructor(
    private val signInWithEmailAndPassword: SignInWithEmailAndPasswordUseCase,
    private val signInWithGoogle: SignInWithGoogleUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SignInUiState())
    val state = _state.asStateFlow()

    fun handleIntent(intent: SignInIntent) = when (intent) {
        is SignInIntent.InputEmail -> _state.update { it.inputEmail(intent.email) }
        is SignInIntent.InputPassword -> _state.update { it.inputPassword(intent.password) }
        SignInIntent.CheckRememberMe -> _state.update { it.checkRememberMe() }
        SignInIntent.SignIn -> handleSignIn()
        SignInIntent.LaunchSigningInWithGoogle -> _state.update { it.launchSignInWithGoogle() }
        SignInIntent.SigningInWithGoogleLaunched -> _state.update { it.signInWithGoogleLaunched() }
        is SignInIntent.SignInWithGoogleToken -> handleSignInWithGoogleToken(intent.token)
        SignInIntent.CancelSigningInWithGoogle -> _state.update { it.updateIsLoading(false) }
        SignInIntent.SignInErrorShown -> _state.update { it.signInErrorShown() }
        SignInIntent.WrongPasswordErrorShown -> _state.update { it.updateWrongPassword(false) }
        SignInIntent.UserNotFoundErrorShown -> _state.update { it.updateUserNotFound(false) }
        SignInIntent.EmailAssociatedWithGoogleShown -> _state.update {
            it.updateEmailAssociatedWithGoogle(false)
        }

        SignInIntent.WrongPasswordAndEmailAssociatedWithGoogleShown -> _state.update {
            it.updateWrongPasswordAndEmailAssociatedWithGoogle(false)
        }
    }

    private fun handleSignInWithGoogleToken(token: String) {
        viewModelScope.launch {
            when (signInWithGoogle(Token(token))) {
                is Result.Success -> _state.update { it.signedIn() }
                is Result.Error -> _state.update { it.signInError() }
            }
        }
    }

    private fun handleSignIn() {
        viewModelScope.launch {
            _state.update { it.updateIsLoading(true) }
            val result = signInWithEmailAndPassword(
                email = state.value.email.trim(),
                password = state.value.password.trim(),
                rememberMe = state.value.rememberMe
            )
            _state.update { it.updateIsLoading(false) }

            when (result) {
                is Result.Success -> {
                    when (result.data) {
                        SignInWithEmailAndPasswordResult.EmailNotValid -> _state.update {
                            it.emailIsNotValid()
                        }

                        SignInWithEmailAndPasswordResult.WrongPassword -> _state.update {
                            it.updateWrongPassword(true)
                        }

                        SignInWithEmailAndPasswordResult.SignedIn -> _state.update {
                            it.signedIn()
                        }

                        SignInWithEmailAndPasswordResult.EmailAssociatedWithGoogle -> _state.update {
                            it.updateEmailAssociatedWithGoogle(true)
                        }

                        SignInWithEmailAndPasswordResult.UserNotFound -> _state.update {
                            it.updateUserNotFound(true)
                        }

                        SignInWithEmailAndPasswordResult.WrongPasswordAndEmailAssociatedWithGoogle -> _state.update {
                            it.updateWrongPasswordAndEmailAssociatedWithGoogle(true)
                        }
                    }
                }

                is Result.Error -> {
                    result.exception?.printStackTrace()
                    _state.update { it.signInError() }
                }
            }
        }
    }
}