package com.yuriikonovalov.helia.presentation.profile

import android.net.Uri

sealed interface ProfileIntent {
    data class UpdatePhoto(val uri: Uri?) : ProfileIntent
    data class ToggleDarkTheme(val checked: Boolean) : ProfileIntent
    object Logout : ProfileIntent
}