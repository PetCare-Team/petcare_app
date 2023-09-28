package com.example.petcareavance.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/users/sign-in")
    fun signIn(@Body userInfo: UserInfo): Call<UserResponse>
}

data class UserInfo(val mail: String, val password: String)
data class UserResponse(val id: Int)
