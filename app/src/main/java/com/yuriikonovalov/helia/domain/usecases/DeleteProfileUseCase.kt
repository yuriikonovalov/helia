package com.yuriikonovalov.helia.domain.usecases

import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.yuriikonovalov.helia.domain.repositories.UserRepository
import com.yuriikonovalov.helia.utils.Result
import javax.inject.Inject

interface DeleteProfileUseCase {
    suspend operator fun invoke(): Result<DeleteProfileResult>
}

sealed interface DeleteProfileResult {
    object Success : DeleteProfileResult
    object RecentLoginRequired : DeleteProfileResult
}

class DeleteProfileUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : DeleteProfileUseCase {
    override suspend fun invoke(): Result<DeleteProfileResult> {
        return try {
            userRepository.deleteProfile()
            Result.Success(DeleteProfileResult.Success)
        } catch (exception: FirebaseAuthRecentLoginRequiredException) {
            Result.Success(DeleteProfileResult.RecentLoginRequired)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
}