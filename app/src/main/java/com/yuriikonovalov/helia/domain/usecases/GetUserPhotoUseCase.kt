package com.yuriikonovalov.helia.domain.usecases

import android.net.Uri
import com.yuriikonovalov.helia.domain.repositories.UserRepository
import com.yuriikonovalov.helia.utils.Result
import com.yuriikonovalov.helia.utils.asResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetUserPhotoUseCase {
    operator fun invoke(): Flow<Result<Uri?>>
}

class GetUserPhotoUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : GetUserPhotoUseCase {
    override fun invoke(): Flow<Result<Uri?>> {
        return userRepository.observeUserPhoto().asResult()
    }
}