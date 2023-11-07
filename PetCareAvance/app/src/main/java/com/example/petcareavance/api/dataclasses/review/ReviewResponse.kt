package com.example.petcareavance.api.dataclasses.review

import com.example.petcareavance.api.dataclasses.services.ServiceResponse
import com.example.petcareavance.api.dataclasses.users.UserResponse2

data class ReviewResponse(
    val reviewId: Int,
    val service: ServiceResponse,
    val user: UserResponse2,
    val stars: Int,
    val description: String,

    )


