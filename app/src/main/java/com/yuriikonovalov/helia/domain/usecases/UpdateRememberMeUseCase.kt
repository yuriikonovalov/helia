package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.repositories.AppPreferencesRepository
import com.yuriikonovalov.helia.utils.Result
import javax.inject.Inject

interface UpdateRememberMeUseCase {
    suspend operator fun invoke(rememberMe: Boolean): Result<Unit>
}

class UpdateRememberMeUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) : UpdateRememberMeUseCase {
    override suspend fun invoke(rememberMe: Boolean): Result<Unit> {
        return try {
            appPreferencesRepository.updateRememberMe(rememberMe)
            Result.Success(Unit)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
}