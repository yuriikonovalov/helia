package com.yuriikonovalov.helia.presentation.profile.security

data class SecurityUiState(
    val rememberMe: Boolean = false,
    val isLoading: Boolean = false,
    val isRecentLoginRequiredDialogOpen: Boolean = false,
    val isChangePasswordAvailable: Boolean = false,
    val changePassword: Boolean = false
) {
    fun updateIsLoading(value: Boolean) = copy(isLoading = value)
    fun updateIsRecentLoginRequiredDialogOpen(value: Boolean) = copy(
        isRecentLoginRequiredDialogOpen = value
    )

    fun updateRememberMe(value: Boolean) = copy(rememberMe = value)
    fun updateChangePassword(value: Boolean) = copy(changePassword = value)
    fun updateIsChangePasswordAvailable(value: Boolean) = copy(isChangePasswordAvailable = value)
}
