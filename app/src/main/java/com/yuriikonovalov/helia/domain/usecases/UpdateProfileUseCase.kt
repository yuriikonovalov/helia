package com.yuriikonovalov.helia.domain.usecases

import android.net.Uri
import com.yuriikonovalov.helia.domain.repositories.UserRepository
import com.yuriikonovalov.helia.domain.valueobjects.Gender
import com.yuriikonovalov.helia.utils.Result
import java.time.LocalDate
import javax.inject.Inject

interface UpdateProfileUseCase {
    suspend operator fun invoke(
        fullName: String?,
        dateOfBirth: LocalDate?,
        gender: Gender?,
        photoUri: Uri? = null,
        updatePhotoUri: Boolean = true
    ): Result<Unit>
}

class UpdateProfileUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : UpdateProfileUseCase {
    override suspend fun invoke(
        fullName: String?,
        dateOfBirth: LocalDate?,
        gender: Gender?,
        photoUri: Uri?,
        updatePhotoUri: Boolean
    ): Result<Unit> {
        return try {
            userRepository.updateUser(
                fullName = fullName,
                dateOfBirth = dateOfBirth,
                gender = gender
            )

            if (updatePhotoUri) {
                val downloadUri = photoUri?.let { userRepository.uploadPhoto(it) }
                userRepository.updateUserPhoto(downloadUri)
            }
            Result.Success(Unit)
        } catch (exception: Exception) {
            exception.printStackTrace()
            Result.Error(exception)
        }
    }

}

