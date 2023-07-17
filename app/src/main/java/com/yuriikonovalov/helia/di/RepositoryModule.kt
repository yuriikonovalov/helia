package com.yuriikonovalov.helia.di

import com.yuriikonovalov.helia.data.repositories.AppPreferencesRepositoryImpl
import com.yuriikonovalov.helia.data.repositories.HotelRepositoryImpl
import com.yuriikonovalov.helia.data.repositories.SearchQueryRepositoryImpl
import com.yuriikonovalov.helia.data.repositories.UserRepositoryImpl
import com.yuriikonovalov.helia.domain.repositories.AppPreferencesRepository
import com.yuriikonovalov.helia.domain.repositories.HotelRepository
import com.yuriikonovalov.helia.domain.repositories.SearchQueryRepository
import com.yuriikonovalov.helia.domain.repositories.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsAppPreferencesRepository(impl: AppPreferencesRepositoryImpl): AppPreferencesRepository

    @Binds
    fun bindsUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    fun bindsHotelRepository(impl: HotelRepositoryImpl): HotelRepository

    @Binds
    fun bindsSearchQueryRepository(impl: SearchQueryRepositoryImpl): SearchQueryRepository
}