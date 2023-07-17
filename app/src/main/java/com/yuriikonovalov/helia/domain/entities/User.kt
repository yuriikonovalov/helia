package com.yuriikonovalov.helia.domain.entities

import android.net.Uri
import com.yuriikonovalov.helia.domain.valueobjects.Gender
import java.time.LocalDate

data class User(
    val email: String?,
    val fullName: String?,
    val photoUri: Uri?,
    val gender: Gender?,
    val dateOfBirth: LocalDate?
)