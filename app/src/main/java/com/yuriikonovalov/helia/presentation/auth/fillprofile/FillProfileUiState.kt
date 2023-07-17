package com.yuriikonovalov.helia.presentation.auth.fillprofile

import android.net.Uri
import com.yuriikonovalov.helia.domain.valueobjects.Gender
import java.time.LocalDate

data class FillProfileUiState(
    val photoUri: Uri? = null,
    val fullName: String = "",
    val dateOfBirth: LocalDate? = null,
    val gender: Gender? = null,
    val isDatePickerOpen: Boolean = false,
    val isEditPhotoDialogOpen: Boolean = false,
    val isLoading: Boolean = false,
    val profileUpdated: Boolean = false,
    val isError: Boolean = false
) {
    val isContinueButtonEnabled
        get() = fullName.isNotBlank() && dateOfBirth != null && gender != null

    fun inputFullName(value: String) = copy(fullName = value)
    fun updatePhotoUri(value: Uri?) = copy(photoUri = value)
    fun selectGender(value: Gender) = copy(gender = value)
    fun selectDateOfBirth(value: LocalDate) = copy(dateOfBirth = value, isDatePickerOpen = false)
    fun updateIsDatePickerOpen(value: Boolean) = copy(isDatePickerOpen = value)
    fun updateIsLoading(value: Boolean) = copy(isLoading = value)
    fun profileUpdated() = copy(profileUpdated = true)
    fun updateIsError(value: Boolean) = copy(isError = value)
}
