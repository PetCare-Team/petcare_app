package com.example.petcareavance.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.petcareavance.views.User

@Entity(tableName = "service_room")
data class serviceroom(
    @PrimaryKey val serviceId: Int,
    val price: Int,
    val description: String,
    val location: String,
    val phone: Int,
    val dni: Int,
    val cuidador: Boolean,
    @Embedded(prefix = "user_") val user: User
)
