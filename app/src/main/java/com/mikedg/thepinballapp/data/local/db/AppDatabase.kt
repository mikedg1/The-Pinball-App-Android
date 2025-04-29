package com.mikedg.thepinballapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mikedg.thepinballapp.data.local.dao.EventMachineDao
import com.mikedg.thepinballapp.data.local.entity.EventMachine

/**
 * Main database class for the application.
 * Defines the database configuration and serves as the main access point for the database connection.
 */
@Database(
    entities = [EventMachine::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    /**
     * Provides access to the EventMachineDao.
     */
    abstract fun EventMachineDao(): EventMachineDao
}