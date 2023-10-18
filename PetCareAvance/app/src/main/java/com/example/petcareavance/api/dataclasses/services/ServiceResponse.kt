package com.example.petcareavance.api.dataclasses.services

import com.example.petcareavance.api.dataclasses.users.User

data class ServiceResponse(
    val serviceId: Int,
    val price: Int,
    val description: String,
    val location: String,
    val phone: Int,
    val dni: Int,
    val cuidador: Boolean,
    val user: User,
)