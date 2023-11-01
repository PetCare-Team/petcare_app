package com.example.petcareavance.api.dataclasses.review

data class ReviewResponse(
    val serviceId: Int,
    val userId: Int,
    val stars: Int,
    val description: String,

    )


