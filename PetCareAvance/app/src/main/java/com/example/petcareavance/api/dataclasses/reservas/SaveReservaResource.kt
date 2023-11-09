package com.example.petcareavance.api.dataclasses.reservas

import com.example.petcareavance.api.dataclasses.payment.PaymentResponse
import com.example.petcareavance.api.dataclasses.services.ServiceResponse

class SaveReservaResource (

    val date: String,
    val startHour: String,
    val endHour: String,
    val estadoId: Int,
    val serviceProviderId: Int,
    val clientPaymentId: Int



)