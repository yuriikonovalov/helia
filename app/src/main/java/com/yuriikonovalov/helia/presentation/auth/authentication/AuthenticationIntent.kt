package com.yuriikonovalov.helia.presentation.auth.authentication

sealed interface AuthenticationIntent {
    object LaunchSigningInWithGoogle : AuthenticationIntent
    object SigningInWithGoogleLaunched : AuthenticationIntent
    object CancelSigningInWithGoogle : AuthenticationIntent
    data class SignInWithGoogleToken(val token: String) : AuthenticationIntent
    object SignInWithGoogleErrorShown : AuthenticationIntent
}