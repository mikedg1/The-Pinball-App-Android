package com.mikedg.thepinballapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing an EventMachine in the Room database.
 *
 * @property photoFile Path to the photo file associated with this machine
 * @property rowId Unique identifier for the row
 * @property order Timestamp representing the order
 * @property itemId Identifier used when requesting a run
 * @property machineName Name of the machine
 * @property processing Boolean flag indicating if the machine is currently processing
 */
@Entity(tableName = "event_machines")
data class EventMachine(
    @PrimaryKey
    val rowId: String,
    val photoFile: String,
    val order: Long, // Using Long to store timestamp
    val itemId: String,
    val machineName: String,
    val processing: Boolean
)