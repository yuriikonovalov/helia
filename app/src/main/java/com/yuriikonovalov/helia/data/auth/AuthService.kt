package com.yuriikonovalov.helia.data.auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.SignInMethodQueryResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthService {
    val currentUser: FirebaseUser?

    suspend fun signInWithCredential(credential: AuthCredential): AuthResult
    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult
    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult
    suspend fun fetchSignInMethodsForEmail(email: String): SignInMethodQueryResult
    suspend fun signOut()
}

class FirebaseAuthService @Inject constructor(
    private val auth: FirebaseAuth
) : AuthService {
    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun signInWithCredential(credential: AuthCredential): AuthResult {
        return auth.signInWithCredential(credential).await()
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): AuthResult {
        return auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun fetchSignInMethodsForEmail(email: String): SignInMethodQueryResult {
        return auth.fetchSignInMethodsForEmail(email).await()
    }

    override suspend fun signOut() {
        auth.signOut()
    }


}