package com.mikedg.thepinballapp.hilt

import android.content.Context
import androidx.room.Room
import com.mikedg.thepinballapp.data.local.dao.EventMachineDao
import com.mikedg.thepinballapp.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides database-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    private const val DATABASE_NAME = "pinball_app_database"
    
    /**
     * Provides the Room database instance.
     * 
     * @param context The application context
     * @return The AppDatabase instance
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        )
        .fallbackToDestructiveMigration() // Recreate database if no migration path
        .build()
    }
    
    /**
     * Provides the EventMachineDao.
     * 
     * @param database The AppDatabase instance
     * @return The EventMachineDao
     */
    @Provides
    @Singleton
    fun provideEventMachineDao(database: AppDatabase): EventMachineDao {
        return database.EventMachineDao()
    }
}