package com.yuriikonovalov.helia.data.database.user.model

import android.net.Uri
import com.yuriikonovalov.helia.domain.entities.User
import com.yuriikonovalov.helia.domain.valueobjects.Gender
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private fun LocalDate?.toISO(): String = DateTimeFormatter.ISO_DATE.format(this)
private fun String.fromISO() = LocalDate.parse(this, DateTimeFormatter.ISO_DATE)

data class UserDocument(
    val fullName: String? = null,
    val email: String? = null,
    val gender: Gender? = null,
    val dateOfBirth: String? = null,
    val photoUri: String? = null
) {
    fun toDomain() = User(
        email = email,
        fullName = fullName,
        photoUri = photoUri?.let { Uri.parse(photoUri) },
        gender = gender,
        dateOfBirth = dateOfBirth?.fromISO()
    )

    companion object {
        fun fromDomain(user: User) = UserDocument(
            fullName = user.fullName,
            email = user.email,
            gender = user.gender,
            dateOfBirth = user.dateOfBirth?.toISO(),
            photoUri = user.photoUri?.toString()
        )
    }
}



