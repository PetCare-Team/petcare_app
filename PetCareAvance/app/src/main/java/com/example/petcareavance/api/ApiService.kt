package com.example.petcareavance.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("api/v1/users/sign-in")
    fun signIn(@Body userInfo: UserInfo): Call<UserResponse>
    @POST("api/v1/users/sign-up")
    fun signUp(@Body userSignInInfo: UserSignInInfo): Call<UserResponseForSingUp>
    @GET("api/v1/userId/pets")
    fun getPetByUser(@Header("Authorization")
                     token: String,@Path("userId") userId: String): Call<List<PetResponse>>
}

data class UserInfo(val mail: String, val password: String)

data class UserSignInInfo(val mail: String, val password: String, val firstName: String,
                          val lastName: String,val phone: Int, val typeUserId: Int, val dni: Int )
data class UserResponse(val id: Int, val token: String)
data class UserResponseForSingUp(val message: String)

data class PetResponse(val id: Int, val name: String)

