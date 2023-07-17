package com.yuriikonovalov.helia.data.database.user

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.yuriikonovalov.helia.data.database.user.model.UserDocument
import com.yuriikonovalov.helia.domain.entities.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface UserDatabaseService {
    suspend fun getUser(id: String): User?
    suspend fun updateUser(id: String, user: User)
    suspend fun setUserNameAndEmail(id: String, fullName: String?, email: String)
    fun observeUserPhoto(id: String): Flow<Uri?>
    suspend fun updateUserPhoto(id: String, photoUri: Uri?)
    suspend fun deleteUserDocument(id: String)
    fun observeUser(id: String): Flow<User?>
}

class FirestoreUserDatabaseService @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserDatabaseService {
    override suspend fun getUser(id: String): User? {
        return firestore.collection(FirestoreUserConfig.Collections.USERS)
            .document(id)
            .get()
            .await()
            .toObject(UserDocument::class.java)
            ?.toDomain()
    }

    override suspend fun updateUser(id: String, user: User) {
        val userDocument = UserDocument.fromDomain(user)
        val data = mapOf(
            "email" to userDocument.email,
            "fullName" to userDocument.fullName,
            "dateOfBirth" to userDocument.dateOfBirth,
            "gender" to userDocument.gender,
        )
        firestore.collection(FirestoreUserConfig.Collections.USERS)
            .document(id)
            .set(data, SetOptions.merge())
            .await()
    }

    override suspend fun setUserNameAndEmail(id: String, fullName: String?, email: String) {
        val data = mapOf(
            "email" to email,
            "fullName" to fullName
        )
        firestore.collection(FirestoreUserConfig.Collections.USERS)
            .document(id)
            .set(data, SetOptions.merge())
            .await()
    }


    override fun observeUserPhoto(id: String): Flow<Uri?> {
        val userRef = firestore
            .collection(FirestoreUserConfig.Collections.USERS)
            .document(id)

        // TODO: DELETE and use OBSERVE USER
        return callbackFlow {
            userRef.addSnapshotListener { value, error ->
                val uriString = value?.getString("photoUri")
                val uri = uriString?.let { Uri.parse(uriString) }
                trySend(uri)
            }
            awaitClose { }
        }
    }

    override suspend fun updateUserPhoto(id: String, photoUri: Uri?) {
        firestore
            .collection(FirestoreUserConfig.Collections.USERS)
            .document(id)
            .set(mapOf("photoUri" to photoUri), SetOptions.merge())
            .await()
    }

    override suspend fun deleteUserDocument(id: String) {
        firestore.collection(FirestoreUserConfig.Collections.USERS)
            .document(id)
            .delete()
            .await()
    }

    override fun observeUser(id: String): Flow<User?> {
        val userRef = firestore
            .collection(FirestoreUserConfig.Collections.USERS)
            .document(id)

        return callbackFlow {
            val registration = userRef.addSnapshotListener { value, error ->
                val userDocument = value?.toObject<UserDocument>()
                val user = userDocument?.toDomain()
                trySend(user)
            }
            awaitClose {
                registration.remove()
            }
        }
    }
}