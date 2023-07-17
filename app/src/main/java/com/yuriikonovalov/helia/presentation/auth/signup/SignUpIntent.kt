package com.yuriikonovalov.helia.presentation.auth.signup


sealed interface SignUpIntent {
    data class InputEmail(val email: String) : SignUpIntent
    data class InputPassword(val password: String) : SignUpIntent
    object CheckRememberMe : SignUpIntent
    object SignUp : SignUpIntent
    object CloseAccountAlreadyExistDialog : SignUpIntent
    object CloseEmailAlreadyInUseWithGoogleDialog : SignUpIntent
    object LaunchSigningInWithGoogle : SignUpIntent
    object SigningInWithGoogleLaunched : SignUpIntent
    object CancelSigningInWithGoogle : SignUpIntent
    data class SignInWithGoogleToken(val token: String) : SignUpIntent
    object SignInErrorShown : SignUpIntent
}