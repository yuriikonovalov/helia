package com.yuriikonovalov.helia.presentation.profile.security.changepassword

sealed interface ChangePasswordIntent {
    data class InputCurrentPassword(val password: String) : ChangePasswordIntent
    data class InputNewPassword(val password: String) : ChangePasswordIntent
    object Change : ChangePasswordIntent
    object ErrorShown : ChangePasswordIntent
    object RecentLoginRequiredDialogDismissed : ChangePasswordIntent
    object SignOut : ChangePasswordIntent
}