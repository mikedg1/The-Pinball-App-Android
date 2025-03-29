package com.mikedg.thepinballapp.hilt

import com.mikedg.thepinballapp.data.remote.OpdbApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOpdbApi(): OpdbApiService {
        return OpdbApiService()
    }
}