package com.mikedg.thepinballapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mikedg.thepinballapp.data.local.entity.EventMachine
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for EventMachine entities.
 * Provides methods to interact with the event_machines table in the database.
 */
@Dao
interface EventMachineDao {
    
    /**
     * Insert a new EventMachine into the database.
     * If a machine with the same rowId already exists, it will be replaced.
     * 
     * @param machine The EventMachine to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMachine(machine: EventMachine)
    
    /**
     * Insert multiple EventMachines into the database.
     * If machines with the same rowIds already exist, they will be replaced.
     * 
     * @param machines The list of EventMachines to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMachines(machines: List<EventMachine>)
    
    /**
     * Update an existing EventMachine in the database.
     * 
     * @param machine The EventMachine to update
     */
    @Update
    suspend fun updateMachine(machine: EventMachine)
    
    /**
     * Delete an EventMachine from the database.
     * 
     * @param machine The EventMachine to delete
     */
    @Delete
    suspend fun deleteMachine(machine: EventMachine)
    
    /**
     * Get an EventMachine by its rowId.
     * 
     * @param rowId The rowId of the machine to retrieve
     * @return The EventMachine with the specified rowId, or null if not found
     */
    @Query("SELECT * FROM event_machines WHERE rowId = :rowId")
    suspend fun getMachineByRowId(rowId: String): EventMachine?
    
    /**
     * Get an EventMachine by its itemId.
     * 
     * @param itemId The itemId of the machine to retrieve
     * @return The EventMachine with the specified itemId, or null if not found
     */
    @Query("SELECT * FROM event_machines WHERE itemId = :itemId")
    suspend fun getMachineByItemId(itemId: String): EventMachine?
    
    /**
     * Get all EventMachines from the database.
     * 
     * @return A Flow emitting a list of all EventMachines
     */
    @Query("SELECT * FROM event_machines")
    fun getAllMachines(): Flow<List<EventMachine>>
    
    /**
     * Get all EventMachines ordered by the 'order' field (timestamp).
     * 
     * @return A Flow emitting a list of all EventMachines ordered by timestamp
     */
    @Query("SELECT * FROM event_machines ORDER BY `order` ASC")
    fun getAllMachinesByOrder(): Flow<List<EventMachine>>
    
    /**
     * Get all EventMachines that are currently processing.
     * 
     * @return A Flow emitting a list of all EventMachines where processing is true
     */
    @Query("SELECT * FROM event_machines WHERE processing = 1")
    fun getProcessingMachines(): Flow<List<EventMachine>>
    
    /**
     * Update the processing status of a machine.
     * 
     * @param rowId The rowId of the machine to update
     * @param processing The new processing status
     */
    @Query("UPDATE event_machines SET processing = :processing WHERE rowId = :rowId")
    suspend fun updateProcessingStatus(rowId: String, processing: Boolean)
    
    /**
     * Delete all EventMachines from the database.
     */
    @Query("DELETE FROM event_machines")
    suspend fun deleteAllMachines()
}