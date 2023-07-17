package com.yuriikonovalov.helia.data.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface Storage {
    suspend fun uploadPhoto(uid: String, uri: Uri): Uri
    suspend fun deletePhoto(uid: String)
}

class FirebaseCloudStorage @Inject constructor(
    private val storage: FirebaseStorage
) : Storage {
    override suspend fun uploadPhoto(uid: String, uri: Uri): Uri {
        val ref = storage.getReference(StorageConfig.PROFILE_IMAGE_REF + "/" + uid)
        ref.putFile(uri).await()

        return ref.downloadUrl.await()!!
    }

    override suspend fun deletePhoto(uid: String) {
        storage
            .getReference(StorageConfig.PROFILE_IMAGE_REF + "/" + uid)
            .delete()
            .await()
    }
}