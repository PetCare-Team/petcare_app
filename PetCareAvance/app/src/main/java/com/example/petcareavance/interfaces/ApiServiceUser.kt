package com.example.petcareavance.interfaces

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiServiceUser {
    @GET("api/v1/users/{userId}")
    fun getUserProfile(
        @Header("Authorization")
        token: String,
        @Path("userId") userId: String
    ): Call<UserResponse2>
}

data class UserResponse2(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phone: Int,
    val dni: Int,
    val typeUser: TypeUser,
    val mail: String
)

data class TypeUser(
    val typeUserID: Int,
    val type: String
)
