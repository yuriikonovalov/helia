package com.yuriikonovalov.helia.presentation.profile.security.changepassword

data class ChangePasswordUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val isWrongPassword: Boolean = false,
    val isWeakPassword: Boolean = false,
    val isRecentLoginRequiredDialogOpen: Boolean = false,
    val isLoading: Boolean = false,
    val isChanged: Boolean = false,
    val isError: Boolean = false
) {
    val isChangeButtonEnabled: Boolean
        get() = currentPassword.isNotBlank() && newPassword.length > 5

    fun updateCurrentPassword(value: String) = copy(
        currentPassword = value,
        isWrongPassword = false
    )

    fun updateNewPassword(value: String) = copy(
        newPassword = value,
        isWeakPassword = false
    )

    fun updateIsLoading(value: Boolean) = copy(isLoading = value)
    fun updateIsChanged(value: Boolean) = copy(isChanged = value)
    fun updateIsError(value: Boolean) = copy(isError = value)
    fun updateIsWrongPassword(value: Boolean) = copy(isWrongPassword = value)
    fun updateIsWeakPassword(value: Boolean) = copy(isWeakPassword = value)
    fun updateIsRecentLoginRequiredDialogOpen(value: Boolean) = copy(isRecentLoginRequiredDialogOpen = value)

}
