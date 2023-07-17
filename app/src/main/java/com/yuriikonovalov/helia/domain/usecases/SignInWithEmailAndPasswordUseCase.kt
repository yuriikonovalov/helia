package com.yuriikonovalov.helia.domain.usecases

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.yuriikonovalov.helia.domain.repositories.AppPreferencesRepository
import com.yuriikonovalov.helia.domain.repositories.UserRepository
import com.yuriikonovalov.helia.domain.usecases.SignInWithEmailAndPasswordResult.*
import com.yuriikonovalov.helia.utils.Result
import javax.inject.Inject

interface SignInWithEmailAndPasswordUseCase {
    suspend operator fun invoke(
        email: String,
        password: String,
        rememberMe: Boolean
    ): Result<SignInWithEmailAndPasswordResult>
}

sealed interface SignInWithEmailAndPasswordResult {
    object EmailNotValid : SignInWithEmailAndPasswordResult
    object UserNotFound : SignInWithEmailAndPasswordResult
    object WrongPassword : SignInWithEmailAndPasswordResult
    object EmailAssociatedWithGoogle : SignInWithEmailAndPasswordResult
    object WrongPasswordAndEmailAssociatedWithGoogle : SignInWithEmailAndPasswordResult
    object SignedIn : SignInWithEmailAndPasswordResult

}

class SignInWithEmailAndPasswordUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val appPreferencesRepository: AppPreferencesRepository,
    private val validateEmail: ValidateEmailUseCase
) : SignInWithEmailAndPasswordUseCase {
    override suspend fun invoke(
        email: String,
        password: String,
        rememberMe: Boolean
    ): Result<SignInWithEmailAndPasswordResult> {

        if (!validateEmail(email)) return Result.Success(EmailNotValid)

        return try {
            userRepository.signInWithEmailAndPassword(email, password)
            appPreferencesRepository.updateRememberMe(rememberMe)
            Result.Success(SignedIn)
        } catch (exception: FirebaseAuthInvalidCredentialsException) {
            if (exception.errorCode == "ERROR_WRONG_PASSWORD") {
                val signInMethods = userRepository.getSignInMethodsForEmail(email)
                when {
                    onlyGoogleProvider(signInMethods) -> Result.Success(EmailAssociatedWithGoogle)
                    emailAndGoogleProvider(signInMethods) -> Result.Success(
                        WrongPasswordAndEmailAssociatedWithGoogle
                    )

                    else -> Result.Success(WrongPassword) // only EmailAuthProvider case
                }
            } else {
                throw exception
            }
        } catch (exception: FirebaseAuthInvalidUserException) {
            Result.Success(UserNotFound)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

    private fun onlyGoogleProvider(signInMethods: List<String>): Boolean {
        return signInMethods.contains(GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD) && signInMethods.size == 1
    }

    private fun emailAndGoogleProvider(signInMethods: List<String>): Boolean {
        return signInMethods.contains(GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD)
                && signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)
    }
}