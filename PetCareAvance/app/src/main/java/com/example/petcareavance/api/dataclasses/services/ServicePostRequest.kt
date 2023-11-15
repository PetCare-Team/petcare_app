package com.example.petcareavance.api.dataclasses.services

data class ServicePostRequest(
    val price: Int,
    val description: String,
    val location: String,
    val phone: Int,
    val dni: Int,
    val cuidador: Boolean,
    val userId: Int
)

