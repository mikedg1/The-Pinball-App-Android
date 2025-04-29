package com.mikedg.thepinballapp.data.repository

import com.mikedg.thepinballapp.data.local.dao.EventMachineDao
import com.mikedg.thepinballapp.data.local.entity.EventMachine
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository class that provides a clean API for accessing EventMachine data.
 * It abstracts the data sources from the rest of the app.
 */
@Singleton
class EventMachineRepository @Inject constructor(
    private val EventMachineDao: EventMachineDao
) {
    /**
     * Get all machines from the database.
     * 
     * @return A Flow emitting a list of all EventMachines
     */
    fun getAllMachines(): Flow<List<EventMachine>> {
        return EventMachineDao.getAllMachines()
    }
    
    /**
     * Get all machines ordered by timestamp.
     * 
     * @return A Flow emitting a list of all EventMachines ordered by timestamp
     */
    fun getAllMachinesByOrder(): Flow<List<EventMachine>> {
        return EventMachineDao.getAllMachinesByOrder()
    }
    
    /**
     * Get all machines that are currently processing.
     * 
     * @return A Flow emitting a list of all EventMachines where processing is true
     */
    fun getProcessingMachines(): Flow<List<EventMachine>> {
        return EventMachineDao.getProcessingMachines()
    }
    
    /**
     * Get a machine by its rowId.
     * 
     * @param rowId The rowId of the machine to retrieve
     * @return The EventMachine with the specified rowId, or null if not found
     */
    suspend fun getMachineByRowId(rowId: String): EventMachine? {
        return EventMachineDao.getMachineByRowId(rowId)
    }
    
    /**
     * Get a machine by its itemId.
     * 
     * @param itemId The itemId of the machine to retrieve
     * @return The EventMachine with the specified itemId, or null if not found
     */
    suspend fun getMachineByItemId(itemId: String): EventMachine? {
        return EventMachineDao.getMachineByItemId(itemId)
    }
    
    /**
     * Insert a new machine into the database.
     * 
     * @param machine The EventMachine to insert
     */
    suspend fun insertMachine(machine: EventMachine) {
        EventMachineDao.insertMachine(machine)
    }
    
    /**
     * Insert multiple machines into the database.
     * 
     * @param machines The list of EventMachines to insert
     */
    suspend fun insertMachines(machines: List<EventMachine>) {
        EventMachineDao.insertMachines(machines)
    }
    
    /**
     * Update an existing machine in the database.
     * 
     * @param machine The EventMachine to update
     */
    suspend fun updateMachine(machine: EventMachine) {
        EventMachineDao.updateMachine(machine)
    }
    
    /**
     * Delete a machine from the database.
     * 
     * @param machine The EventMachine to delete
     */
    suspend fun deleteMachine(machine: EventMachine) {
        EventMachineDao.deleteMachine(machine)
    }
    
    /**
     * Update the processing status of a machine.
     * 
     * @param rowId The rowId of the machine to update
     * @param processing The new processing status
     */
    suspend fun updateProcessingStatus(rowId: String, processing: Boolean) {
        EventMachineDao.updateProcessingStatus(rowId, processing)
    }
    
    /**
     * Delete all machines from the database.
     */
    suspend fun deleteAllMachines() {
        EventMachineDao.deleteAllMachines()
    }
}