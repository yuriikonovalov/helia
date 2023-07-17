package com.yuriikonovalov.helia.presentation.auth.signup

data class SignUpUiState(
    val email: String = "",
    val isValidEmail: Boolean = true,
    val password: String = "",
    val isValidPassword: Boolean = true,
    val rememberMe: Boolean = false,
    val isLoading: Boolean = false,
    val userCreated: Boolean = false,
    val isEmailAlreadyInUseDialogOpen: Boolean = false,
    val isEmailAlreadyInUseWithGoogleDialogOpen: Boolean = false,
    val signInWithGoogle: Boolean = false,
    val signInError: Boolean = false,
) {
    val isSignUpButtonEnabled
        get() = email.isNotEmpty() && isValidEmail && password.isNotEmpty() && isValidPassword

    fun checkRememberMe() = copy(rememberMe = !rememberMe)

    fun userCreated() = copy(userCreated = true)

    fun inputEmail(value: String): SignUpUiState {
        val isValid = value.isEmpty() || value.isNotBlank()
        return copy(email = value, isValidEmail = isValid)
    }

    fun inputPassword(value: String) = copy(password = value, isValidPassword = true)

    fun passwordIsNotValid() = copy(isValidPassword = false)

    fun emailIsNotValid() = copy(isValidEmail = false)

    fun updateIsLoading(loading: Boolean) = copy(isLoading = loading)

    fun updateEmailAlreadyInUseDialogOpen(open: Boolean) = copy(
        isEmailAlreadyInUseDialogOpen = open
    )

    fun updateEmailAlreadyInUseWithGoogleDialogOpen(open: Boolean) = copy(
        isEmailAlreadyInUseWithGoogleDialogOpen = open
    )

    fun launchSignInWithGoogle() = copy(
        isLoading = true,
        signInWithGoogle = true,
        isEmailAlreadyInUseWithGoogleDialogOpen = false
    )

    fun signInWithGoogleLaunched() = copy(signInWithGoogle = false)

    fun signInError() = copy(isLoading = false, signInError = true)

    // When user sign in with Google, we just hiding a loading indicator.
    // Navigation in this case will be handled by the MainActivity
    // when FirebaseAuthListener updates its state.
    fun userSignedInWithGoogle() = copy(isLoading = false)

    fun signInErrorShown() = copy(signInError = false)
}
