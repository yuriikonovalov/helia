package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.entities.User
import com.yuriikonovalov.helia.domain.repositories.UserRepository
import com.yuriikonovalov.helia.utils.Result
import javax.inject.Inject

interface GetUserUseCase {
    suspend operator fun invoke(): Result<User>
}

class GetUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : GetUserUseCase {
    override suspend fun invoke(): Result<User> {
        return try {
            Result.Success(userRepository.getUser())
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
}