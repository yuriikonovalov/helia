package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.repositories.AppPreferencesRepository
import com.yuriikonovalov.helia.domain.repositories.UserRepository
import com.yuriikonovalov.helia.domain.valueobjects.Token
import com.yuriikonovalov.helia.utils.Result
import javax.inject.Inject

interface SignInWithGoogleUseCase {
    suspend operator fun invoke(token: Token): Result<SignInWithGoogleResult>
}

data class SignInWithGoogleResult(
    val isNewUser: Boolean
)

class SignInWithGoogleUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val appPreferencesRepository: AppPreferencesRepository
) : SignInWithGoogleUseCase {
    override suspend fun invoke(token: Token): Result<SignInWithGoogleResult> {
        return try {
            val signInResult = userRepository.authenticateWithGoogle(token.value)
            if (signInResult != null) {
                // receives email and name from the provider and saves in DB
                userRepository.setNameAndEmailFromCurrentUser()
                appPreferencesRepository.updateRememberMe(true)
                Result.Success(SignInWithGoogleResult(isNewUser = signInResult.isNewUser))
            } else {
                Result.Error()
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}