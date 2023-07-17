package com.yuriikonovalov.helia.domain.usecases

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.yuriikonovalov.helia.domain.repositories.UserRepository
import com.yuriikonovalov.helia.utils.Result
import javax.inject.Inject

interface ChangePasswordUseCase {
    suspend operator fun invoke(
        currentPassword: String,
        newPassword: String
    ): Result<ChangePasswordResult>
}

sealed interface ChangePasswordResult {
    object WrongPassword : ChangePasswordResult
    object RecentLoginRequired : ChangePasswordResult
    object WeakPassword : ChangePasswordResult
    object Success : ChangePasswordResult
}

class ChangePasswordUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : ChangePasswordUseCase {
    override suspend fun invoke(
        currentPassword: String,
        newPassword: String
    ): Result<ChangePasswordResult> {
        return try {
            userRepository.updatePassword(currentPassword, newPassword)
            Result.Success(ChangePasswordResult.Success)
        } catch (exception: FirebaseAuthRecentLoginRequiredException) {
            Result.Success(ChangePasswordResult.RecentLoginRequired)
        } catch (exception: FirebaseAuthInvalidCredentialsException) {
            Result.Success(ChangePasswordResult.WrongPassword)
        } catch (exception: FirebaseAuthWeakPasswordException) {
            Result.Success(ChangePasswordResult.WeakPassword)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
}