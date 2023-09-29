package com.example.petcareavance.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/users/sign-in")
    fun signIn(@Body userInfo: UserInfo): Call<UserResponse>
    @POST("api/v1/users/sign-up")
    fun signUp(@Body userSignInInfo: UserSignInInfo): Call<UserResponseForSingUp>
}

data class UserInfo(val mail: String, val password: String)

data class UserSignInInfo(val mail: String, val password: String, val firstName: String,
                          val lastName: String,val phone: Int, val typeUserId: Int, val dni: Int )
data class UserResponse(val id: Int)
data class UserResponseForSingUp(val message: String)
