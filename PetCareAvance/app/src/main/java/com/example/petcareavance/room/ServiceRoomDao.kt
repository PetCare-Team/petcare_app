package com.example.petcareavance.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ServiceRoomDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(serviceRoom: serviceroom)

    @Query("SELECT * FROM service_room")
    fun getAll(): List<com.example.petcareavance.room.serviceroom>

    @Delete
    fun delete(serviceRoom: serviceroom)
}