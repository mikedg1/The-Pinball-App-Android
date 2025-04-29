package com.mikedg.thepinballapp.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mikedg.thepinballapp.data.local.db.AppDatabase
import com.mikedg.thepinballapp.data.local.entity.EventMachine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class EventMachineDaoTest {
    
    private lateinit var EventMachineDao: EventMachineDao
    private lateinit var db: AppDatabase
    
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        EventMachineDao = db.EventMachineDao()
    }
    
    @After
    fun closeDb() {
        db.close()
    }
    
    @Test
    fun insertAndGetMachine() = runBlocking {
        // Create a test machine
        val machine = EventMachine(
            rowId = UUID.randomUUID().toString(),
            photoFile = "test_photo.jpg",
            order = System.currentTimeMillis(),
            itemId = "test_item_id",
            machineName = "Test Machine",
            processing = false
        )
        
        // Insert the machine
        EventMachineDao.insertMachine(machine)
        
        // Get the machine by rowId
        val retrievedMachine = EventMachineDao.getMachineByRowId(machine.rowId)
        
        // Verify the machine was correctly inserted and retrieved
        assertNotNull(retrievedMachine)
        assertEquals(machine.rowId, retrievedMachine?.rowId)
        assertEquals(machine.photoFile, retrievedMachine?.photoFile)
        assertEquals(machine.order, retrievedMachine?.order)
        assertEquals(machine.itemId, retrievedMachine?.itemId)
        assertEquals(machine.machineName, retrievedMachine?.machineName)
        assertEquals(machine.processing, retrievedMachine?.processing)
    }
    
    @Test
    fun updateMachine() = runBlocking {
        // Create and insert a test machine
        val machine = EventMachine(
            rowId = UUID.randomUUID().toString(),
            photoFile = "test_photo.jpg",
            order = System.currentTimeMillis(),
            itemId = "test_item_id",
            machineName = "Test Machine",
            processing = false
        )
        EventMachineDao.insertMachine(machine)
        
        // Update the machine
        val updatedMachine = machine.copy(
            photoFile = "updated_photo.jpg",
            machineName = "Updated Machine",
            processing = true
        )
        EventMachineDao.updateMachine(updatedMachine)
        
        // Get the updated machine
        val retrievedMachine = EventMachineDao.getMachineByRowId(machine.rowId)
        
        // Verify the machine was correctly updated
        assertNotNull(retrievedMachine)
        assertEquals(updatedMachine.photoFile, retrievedMachine?.photoFile)
        assertEquals(updatedMachine.machineName, retrievedMachine?.machineName)
        assertEquals(updatedMachine.processing, retrievedMachine?.processing)
    }
    
    @Test
    fun deleteMachine() = runBlocking {
        // Create and insert a test machine
        val machine = EventMachine(
            rowId = UUID.randomUUID().toString(),
            photoFile = "test_photo.jpg",
            order = System.currentTimeMillis(),
            itemId = "test_item_id",
            machineName = "Test Machine",
            processing = false
        )
        EventMachineDao.insertMachine(machine)
        
        // Delete the machine
        EventMachineDao.deleteMachine(machine)
        
        // Try to get the deleted machine
        val retrievedMachine = EventMachineDao.getMachineByRowId(machine.rowId)
        
        // Verify the machine was deleted
        assertNull(retrievedMachine)
    }
    
    @Test
    fun getAllMachines() = runBlocking {
        // Create and insert multiple test machines
        val machine1 = EventMachine(
            rowId = UUID.randomUUID().toString(),
            photoFile = "photo1.jpg",
            order = System.currentTimeMillis(),
            itemId = "item_id_1",
            machineName = "Machine 1",
            processing = false
        )
        val machine2 = EventMachine(
            rowId = UUID.randomUUID().toString(),
            photoFile = "photo2.jpg",
            order = System.currentTimeMillis() + 1000,
            itemId = "item_id_2",
            machineName = "Machine 2",
            processing = true
        )
        
        EventMachineDao.insertMachines(listOf(machine1, machine2))
        
        // Get all machines
        val allMachines = EventMachineDao.getAllMachines().first()
        
        // Verify all machines were retrieved
        assertEquals(2, allMachines.size)
    }
    
    @Test
    fun getProcessingMachines() = runBlocking {
        // Create and insert multiple test machines with different processing states
        val machine1 = EventMachine(
            rowId = UUID.randomUUID().toString(),
            photoFile = "photo1.jpg",
            order = System.currentTimeMillis(),
            itemId = "item_id_1",
            machineName = "Machine 1",
            processing = false
        )
        val machine2 = EventMachine(
            rowId = UUID.randomUUID().toString(),
            photoFile = "photo2.jpg",
            order = System.currentTimeMillis() + 1000,
            itemId = "item_id_2",
            machineName = "Machine 2",
            processing = true
        )
        val machine3 = EventMachine(
            rowId = UUID.randomUUID().toString(),
            photoFile = "photo3.jpg",
            order = System.currentTimeMillis() + 2000,
            itemId = "item_id_3",
            machineName = "Machine 3",
            processing = true
        )
        
        EventMachineDao.insertMachines(listOf(machine1, machine2, machine3))
        
        // Get processing machines
        val processingMachines = EventMachineDao.getProcessingMachines().first()
        
        // Verify only processing machines were retrieved
        assertEquals(2, processingMachines.size)
        processingMachines.forEach { machine ->
            assertEquals(true, machine.processing)
        }
    }
    
    @Test
    fun updateProcessingStatus() = runBlocking {
        // Create and insert a test machine
        val machine = EventMachine(
            rowId = UUID.randomUUID().toString(),
            photoFile = "test_photo.jpg",
            order = System.currentTimeMillis(),
            itemId = "test_item_id",
            machineName = "Test Machine",
            processing = false
        )
        EventMachineDao.insertMachine(machine)
        
        // Update the processing status
        EventMachineDao.updateProcessingStatus(machine.rowId, true)
        
        // Get the updated machine
        val retrievedMachine = EventMachineDao.getMachineByRowId(machine.rowId)
        
        // Verify the processing status was updated
        assertNotNull(retrievedMachine)
        assertEquals(true, retrievedMachine?.processing)
    }
}