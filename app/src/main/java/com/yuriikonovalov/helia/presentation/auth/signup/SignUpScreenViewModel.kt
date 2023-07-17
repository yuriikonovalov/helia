package com.yuriikonovalov.helia.presentation.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriikonovalov.helia.domain.usecases.SignInWithGoogleUseCase
import com.yuriikonovalov.helia.domain.usecases.SignUpResult
import com.yuriikonovalov.helia.domain.usecases.SignUpWithEmailAndPasswordUseCase
import com.yuriikonovalov.helia.domain.valueobjects.Token
import com.yuriikonovalov.helia.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val signUpWithEmailAndPassword: SignUpWithEmailAndPasswordUseCase,
    private val signInWithGoogle: SignInWithGoogleUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(SignUpUiState())
    val state = _state.asStateFlow()


    fun handleIntent(intent: SignUpIntent) = when (intent) {
        is SignUpIntent.InputEmail -> _state.update { it.inputEmail(intent.email) }
        is SignUpIntent.InputPassword -> _state.update { it.inputPassword(intent.password) }
        SignUpIntent.CheckRememberMe -> _state.update { it.checkRememberMe() }
        SignUpIntent.SignUp -> handleSignUp()
        SignUpIntent.CloseAccountAlreadyExistDialog -> _state.update {
            it.updateEmailAlreadyInUseDialogOpen(false)
        }

        SignUpIntent.CancelSigningInWithGoogle -> _state.update { it.updateIsLoading(false) }
        SignUpIntent.LaunchSigningInWithGoogle -> _state.update { it.launchSignInWithGoogle() }
        is SignUpIntent.SignInWithGoogleToken -> handleSignInWithGoogleToken(intent.token)
        SignUpIntent.SigningInWithGoogleLaunched -> _state.update { it.signInWithGoogleLaunched() }
        SignUpIntent.SignInErrorShown -> _state.update { it.signInErrorShown() }
        SignUpIntent.CloseEmailAlreadyInUseWithGoogleDialog -> _state.update {
            it.updateEmailAlreadyInUseWithGoogleDialogOpen(false)
        }
    }

    private fun handleSignInWithGoogleToken(token: String) {
        viewModelScope.launch {
            when (signInWithGoogle(Token(token))) {
                is Result.Success -> _state.update { it.userSignedInWithGoogle() }
                is Result.Error -> _state.update { it.signInError() }
            }
        }
    }


    private fun handleSignUp() {
        viewModelScope.launch {
            _state.update { it.updateIsLoading(true) }

            val result = signUpWithEmailAndPassword(
                email = state.value.email.trim(),
                password = state.value.password.trim(),
                rememberMe = state.value.rememberMe
            )

            _state.update { it.updateIsLoading(false) }

            when (result) {
                is Result.Success -> {
                    when (result.data) {
                        SignUpResult.EmailNotValid -> _state.update { it.emailIsNotValid() }
                        SignUpResult.PasswordNotValid -> _state.update { it.passwordIsNotValid() }
                        SignUpResult.EmailAlreadyInUse -> _state.update {
                            it.updateEmailAlreadyInUseDialogOpen(true)
                        }

                        SignUpResult.EmailAlreadyInUseWithGoogle -> _state.update {
                            it.updateEmailAlreadyInUseWithGoogleDialogOpen(true)
                        }

                        SignUpResult.UserCreated -> _state.update { it.userCreated() }
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