package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.utils.isValidEmail
import javax.inject.Inject

interface ValidateEmailUseCase {
    operator fun invoke(email: String): Boolean
}

class ValidateEmailUseCaseImpl @Inject constructor() : ValidateEmailUseCase {
    override operator fun invoke(email: String): Boolean {
        return isValidEmail(email)
    }
}
