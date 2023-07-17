package com.yuriikonovalov.helia.presentation.auth.fillprofile

import android.net.Uri
import com.yuriikonovalov.helia.domain.valueobjects.Gender
import java.time.LocalDate

sealed interface FillProfileIntent {
    data class UpdatePhoto(val uri: Uri?) : FillProfileIntent
    data class InputFullName(val fullName: String) : FillProfileIntent
    data class SelectDateOfBirth(val dateOfBirth: LocalDate) : FillProfileIntent
    data class SelectGender(val gender: Gender) : FillProfileIntent

    object Continue : FillProfileIntent
    object ClickDateOfBirth : FillProfileIntent
    object DismissDatePicker : FillProfileIntent
    object ErrorShown : FillProfileIntent
}