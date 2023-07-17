package com.yuriikonovalov.helia.di

import com.yuriikonovalov.helia.data.api.service.TestHotelServiceImpl
import com.yuriikonovalov.helia.data.repositories.HotelService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TestServer {

    @Binds
    @Singleton
    abstract fun providesHotelService(impl: TestHotelServiceImpl): HotelService
}