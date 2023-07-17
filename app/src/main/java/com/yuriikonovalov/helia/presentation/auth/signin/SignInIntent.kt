package com.yuriikonovalov.helia.presentation.auth.signin

sealed interface SignInIntent {
    data class InputEmail(val email: String) : SignInIntent
    data class InputPassword(val password: String) : SignInIntent
    object CheckRememberMe : SignInIntent
    object SignIn : SignInIntent

    object LaunchSigningInWithGoogle : SignInIntent
    object SigningInWithGoogleLaunched : SignInIntent
    object CancelSigningInWithGoogle : SignInIntent
    data class SignInWithGoogleToken(val token: String) : SignInIntent
    object SignInErrorShown : SignInIntent
    object WrongPasswordErrorShown : SignInIntent
    object UserNotFoundErrorShown : SignInIntent
    object EmailAssociatedWithGoogleShown : SignInIntent
    object WrongPasswordAndEmailAssociatedWithGoogleShown : SignInIntent
}