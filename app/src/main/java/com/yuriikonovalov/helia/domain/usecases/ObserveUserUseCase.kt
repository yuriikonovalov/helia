package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.entities.User
import com.yuriikonovalov.helia.domain.repositories.UserRepository
import com.yuriikonovalov.helia.utils.Result
import com.yuriikonovalov.helia.utils.asResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ObserveUserUseCase {
    operator fun invoke(): Flow<Result<User>>
}

class ObserveUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : ObserveUserUseCase {
    override fun invoke(): Flow<Result<User>> {
        return userRepository.observeUser().asResult()
    }
}