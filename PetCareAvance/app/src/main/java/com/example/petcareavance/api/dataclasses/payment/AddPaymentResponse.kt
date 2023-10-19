package com.example.petcareavance.api.dataclasses.payment

data class AddPaymentResponse(
    val name: String,
    val lastName: String,
    val number: String,
    val expiratedDay: String,
    val cvv: Int,
    val userId: Int
)
