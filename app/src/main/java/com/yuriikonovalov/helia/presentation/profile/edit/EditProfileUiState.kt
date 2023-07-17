package com.yuriikonovalov.helia.presentation.profile.edit

import com.yuriikonovalov.helia.domain.valueobjects.Gender
import java.time.LocalDate

data class EditProfileUiState(
    val fullName: String? = null,
    val gender: Gender? = null,
    val dateOfBirth: LocalDate? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isProfileUpdated: Boolean = false
) {
    fun updateFullName(value: String) = copy(fullName = value)
    fun updateGender(value: Gender) = copy(gender = value)
    fun updateDateOfBirth(value: LocalDate) = copy(dateOfBirth = value)
    fun updateIsLoading(value: Boolean) = copy(isLoading = value)
    fun updateIsError(value: Boolean) = copy(isError = value)
    fun updateIsProfileUpdated(value: Boolean) = copy(isProfileUpdated = value)
    fun updateUser(fullName: String?, dateOfBirth: LocalDate?, gender: Gender?) = copy(
        fullName = fullName, dateOfBirth = dateOfBirth, gender = gender
    )
}
