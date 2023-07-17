package com.yuriikonovalov.helia.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: AppDispatcher)

enum class AppDispatcher {
    IO
}


@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Dispatcher(AppDispatcher.IO)
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}