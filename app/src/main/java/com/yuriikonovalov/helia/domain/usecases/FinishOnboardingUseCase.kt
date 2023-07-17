package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.repositories.AppPreferencesRepository
import com.yuriikonovalov.helia.utils.Result
import javax.inject.Inject

interface FinishOnboardingUseCase {
    suspend operator fun invoke(): Result<Unit>
}

class FinishOnboardingUseCaseImpl @Inject constructor(
    private val preferencesRepository: AppPreferencesRepository
) : FinishOnboardingUseCase {
    override suspend fun invoke(): Result<Unit> {
        return try {
            preferencesRepository.updateShowOnboarding(false)
            Result.Success(Unit)
        } catch (e: Throwable) {
            Result.Error(e)
        }
    }
}