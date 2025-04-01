package com.mikedg.thepinballapp.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.Calendar
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Module for utility singletons that can be used very generically
 */
@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {
    @Provides
    @Singleton
    @CalendarNow
    fun provideCalendarNow(): Calendar {
        return Calendar.getInstance()
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CalendarNow
