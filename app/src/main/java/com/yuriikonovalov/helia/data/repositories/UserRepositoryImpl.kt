package com.yuriikonovalov.helia.data.repositories

import android.net.Uri
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.yuriikonovalov.helia.data.auth.AuthService
import com.yuriikonovalov.helia.data.database.user.UserDatabaseService
import com.yuriikonovalov.helia.data.storage.Storage
import com.yuriikonovalov.helia.domain.entities.User
import com.yuriikonovalov.helia.domain.repositories.UserRepository
import com.yuriikonovalov.helia.domain.usecases.SignInWithGoogleResult
import com.yuriikonovalov.helia.domain.valueobjects.Gender
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class UserRepositoryImpl @Inject constructor(
    private val auth: AuthService,
    private val database: UserDatabaseService,
    private val storage: Storage
) : UserRepository {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    private val uid
        get() = currentUser!!.uid
    private val email
        get() = currentUser!!.email

    override suspend fun getUser(): User {
        return database.getUser(uid)!!
    }

    override fun observeUser(): Flow<User> {
        return database
            .observeUser(uid)
            .filterNotNull()
    }

    override suspend fun authenticateWithGoogle(token: String): SignInWithGoogleResult? {
        val credential = GoogleAuthProvider.getCredential(token, null)
        return try {
            val authResult = auth.signInWithCredential(credential)
            val isNewUser = authResult.additionalUserInfo?.isNewUser!!
            SignInWithGoogleResult(isNewUser = isNewUser)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
    }

    override suspend fun signUpWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
    }

    override suspend fun getSignInMethodsForEmail(email: String): List<String> {
        val signInMethodQueryResult = auth.fetchSignInMethodsForEmail(email)
        return signInMethodQueryResult.signInMethods ?: emptyList()
    }

    override suspend fun uploadPhoto(uri: Uri): Uri {
        return storage.uploadPhoto(currentUser!!.uid, uri)
    }

    override suspend fun deletePhoto() {
        storage.deletePhoto(currentUser!!.uid)
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun updateUser(
        fullName: String?,
        dateOfBirth: LocalDate?,
        gender: Gender?
    ) {
        val user = User(
            email = email!!,
            fullName = fullName,
            photoUri = null,
            gender = gender,
            dateOfBirth = dateOfBirth
        )
        database.updateUser(uid, user)
    }

    override suspend fun setNameAndEmailFromCurrentUser() {
        database.setUserNameAndEmail(
            id = uid,
            fullName = currentUser!!.displayName,
            email = currentUser!!.email!!
        )
    }

    override suspend fun updateUserPhoto(photoUri: Uri?) {
        database.updateUserPhoto(uid, photoUri)
    }

    override fun observeUserPhoto(): Flow<Uri?> {
        return database.observeUserPhoto(uid)
    }

    override suspend fun deleteProfile() {
        database.deleteUserDocument(uid)
        storage.deletePhoto(uid)
        auth.currentUser?.delete()
    }

    override suspend fun updatePassword(currentPassword: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email!!, currentPassword)
        // if the credential is not correct, then FirebaseAuthInvalidCredentialsException will be thrown.
        auth.currentUser!!.reauthenticate(credential).await()

        auth.currentUser!!.updatePassword(password)
    }
}