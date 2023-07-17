package com.yuriikonovalov.helia.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.yuriikonovalov.helia.data.auth.AuthService
import com.yuriikonovalov.helia.data.auth.FirebaseAuthService
import com.yuriikonovalov.helia.data.database.hotel.FirestoreHotelDatabaseService
import com.yuriikonovalov.helia.data.database.hotel.HotelDatabaseService
import com.yuriikonovalov.helia.data.database.user.FirestoreUserDatabaseService
import com.yuriikonovalov.helia.data.database.user.UserDatabaseService
import com.yuriikonovalov.helia.data.storage.FirebaseCloudStorage
import com.yuriikonovalov.helia.data.storage.Storage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    companion object {
        @Provides
        @Singleton
        fun providesFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        @Provides
        @Singleton
        fun providesFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

        @Provides
        @Singleton
        fun providesStorage(): FirebaseStorage = FirebaseStorage.getInstance()
    }

    @Binds
    fun bindsAuthService(impl: FirebaseAuthService): AuthService

    @Binds
    fun bindsUserDatabaseService(impl: FirestoreUserDatabaseService): UserDatabaseService

    @Binds
    fun bindsHotelDatabaseService(impl: FirestoreHotelDatabaseService): HotelDatabaseService

    @Binds
    fun bindsStorage(impl: FirebaseCloudStorage): Storage
}