package com.yuriikonovalov.helia.domain.usecases

import javax.inject.Inject

interface ValidatePasswordUseCase {
    operator fun invoke(password: String): Boolean
}

class ValidatePasswordUseCaseImpl @Inject constructor() : ValidatePasswordUseCase {
    override fun invoke(password: String): Boolean {
        // When the password is shorter, Firebase throws FirebaseAuthWeakPasswordException.
        return password.length >= 6
    }
}