package com.yuriikonovalov.helia.presentation.profile

import android.net.Uri

data class ProfileScreenUiState(
    val isPhotoUploading: Boolean = false,
    val isUserLoading: Boolean = false,
    val photoUri: Uri? = null,
    val name: String? = null,
    val email: String = "",
    val darkTheme: Boolean = false
) {
    fun updateIsPhotoUploading(value: Boolean) = copy(isPhotoUploading = value)
    fun updateIsUserLoading(value: Boolean) = copy(isUserLoading = value)
    fun updateProfile(name: String?, email: String) = copy(name = name, email = email)
    fun updatePhotoUri(uri: Uri?) = copy(photoUri = uri)
    fun updateIsDarkTheme(value: Boolean) = copy(darkTheme = value)
}
