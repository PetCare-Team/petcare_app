package com.example.petcareavance.api.services

import com.example.petcareavance.api.dataclasses.services.ServiceResponse
import com.example.petcareavance.api.dataclasses.pets.PetResponse
import com.example.petcareavance.api.dataclasses.users.UserInfo
import com.example.petcareavance.api.dataclasses.users.UserResponse
import com.example.petcareavance.api.dataclasses.users.UserResponse2
import com.example.petcareavance.api.dataclasses.users.UserResponseForSingUp
import com.example.petcareavance.api.dataclasses.users.UserSignInInfo
import com.example.petcareavance.api.dataclasses.users.UserUpdate
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("api/v1/users/sign-in")
    fun signIn(@Body userInfo: UserInfo): Call<UserResponse>
    @POST("api/v1/users/sign-up")
    fun signUp(@Body userSignInInfo: UserSignInInfo): Call<UserResponseForSingUp>
    @GET("api/v1/pet/{userId}/pet")
    fun getPetByUser(@Header("Authorization")
                     token: String,@Path("userId") userId: String): Call<List<PetResponse>>
    @PUT("api/v1/users/{userId}")
    fun updateUserInfo(@Header("Authorization") token: String,
                       @Path("userId") userId: String,
                       @Body userUpdate: UserUpdate): Call<UserUpdate>
    @GET("api/v1/users/{userId}")
    fun getUserProfile(
        @Header("Authorization")
        token: String,
        @Path("userId") userId: String
    ): Call<UserResponse2>
    @GET("api/v1/services")
    fun getServices(): Call<List<ServiceResponse>>
}