package com.example.petcareavance.api.dataclasses.payment

data class PaymentResponse(
    val id: Int,
    val name: String,
    val lastName: String,
    val number: String,
    val expiratedDay: String,
    val cvv: Int,
    val userId: Int
)
