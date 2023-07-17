package com.yuriikonovalov.helia.domain.usecases

import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.yuriikonovalov.helia.domain.repositories.AppPreferencesRepository
import com.yuriikonovalov.helia.domain.repositories.UserRepository
import com.yuriikonovalov.helia.utils.Result
import javax.inject.Inject


interface SignUpWithEmailAndPasswordUseCase {
    suspend operator fun invoke(
        email: String,
        password: String,
        rememberMe: Boolean
    ): Result<SignUpResult>
}

sealed interface SignUpResult {
    object UserCreated : SignUpResult
    object EmailAlreadyInUse : SignUpResult
    object EmailAlreadyInUseWithGoogle : SignUpResult
    object EmailNotValid : SignUpResult
    object PasswordNotValid : SignUpResult
}


class SignUpWithEmailAndPasswordUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val appPreferencesRepository: AppPreferencesRepository,
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase,
) : SignUpWithEmailAndPasswordUseCase {
    override suspend fun invoke(
        email: String,
        password: String,
        rememberMe: Boolean
    ): Result<SignUpResult> {
        if (!validateEmail(email)) return Result.Success(SignUpResult.EmailNotValid)
        if (!validatePassword(password)) return Result.Success(SignUpResult.PasswordNotValid)

        return try {
            userRepository.signUpWithEmailAndPassword(email, password)
            userRepository.setNameAndEmailFromCurrentUser()
            appPreferencesRepository.updateRememberMe(rememberMe)
            Result.Success(SignUpResult.UserCreated)
        } catch (exception: Exception) {
            Result.Success(mapAuthExceptionToSignUpResult(exception = exception, email = email))
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }


    private suspend fun mapAuthExceptionToSignUpResult(exception: Exception, email: String) =
        when (exception) {
            is FirebaseAuthUserCollisionException -> mapAuthCollisionOrRethrow(email, exception)
            else -> throw exception
        }


    /**
     * Decides what kind of email use exists ([SignUpResult.EmailAlreadyInUse], [SignUpResult.EmailAlreadyInUseWithGoogle]).
     *
     * The exception will be rethrown if the errorCode of the [exception] is not "ERROR_EMAIL_ALREADY_IN_USE".
     */
    private suspend fun mapAuthCollisionOrRethrow(
        email: String,
        exception: FirebaseAuthUserCollisionException
    ) = when (exception.errorCode) {
        "ERROR_EMAIL_ALREADY_IN_USE" -> {
            val signInMethods = userRepository.getSignInMethodsForEmail(email)
            if (signInMethods.contains(GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD)) {
                SignUpResult.EmailAlreadyInUseWithGoogle
            } else {
                SignUpResult.EmailAlreadyInUse
            }
        }

        else -> throw exception
    }
}