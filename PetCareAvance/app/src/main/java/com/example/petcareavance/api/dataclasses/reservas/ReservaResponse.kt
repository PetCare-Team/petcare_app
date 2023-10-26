package com.example.petcareavance.api.dataclasses.reservas

import com.example.petcareavance.api.dataclasses.payment.PaymentResponse
import com.example.petcareavance.api.dataclasses.services.ServiceResponse

data class ReservaResponse(
    val id: Int,
    val date: String,
    val startHour: String,
    val endHour: String,
    val estadoId: Int,
    val serviceProvider: ServiceResponse,
    val clientPayment: PaymentResponse
)
