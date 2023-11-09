package com.example.petcareavance.api

import com.example.petcareavance.api.dataclasses.payment.AddPaymentResponse
import com.example.petcareavance.api.dataclasses.payment.PaymentResponse
import com.example.petcareavance.api.dataclasses.pets.SavePetResource
import com.example.petcareavance.api.dataclasses.reservas.ReservaResponse
import com.example.petcareavance.api.dataclasses.reservas.SaveReservaResource
import com.example.petcareavance.api.dataclasses.review.ReviewResponse
import com.example.petcareavance.api.dataclasses.services.ServiceResponse
import com.example.petcareavance.api.dataclasses.users.UserResponse2
import com.example.petcareavance.api.dataclasses.users.UserUpdate
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    @PUT("api/v1/users/{userId}")
    fun updateUserInfo(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body userUpdate: UserUpdate
    ): Call<UserUpdate>



    fun getUserProfile(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Call<UserResponse2>

    @GET("api/v1/pet/{userId}/pet")
    fun getPetByUser(@Header("Authorization") token: String,
                     @Path("userId") userId: String): Call<List<PetResponse>>



    @GET("api/v1/services/{id}")
    fun getServiceById( @Path("userId") userId: String): Call<ServiceResponse>

    @GET("api/v1/user/{userId}/payment")
    fun getPaymentByUser(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Call<List<PaymentResponse>>

    @POST("api/v1/payment")
    fun postPayment(@Body addPaymentResponse: AddPaymentResponse): Call<AddPaymentResponse>

    @DELETE("api/v1/payment/{id}")
    fun deletePayment(@Path("id") id: String): Call<Void>


    @GET("api/v1/pet/{id}")
    fun getPetById(@Path("id") id: String): Call<PetResponse>
    @PUT("api/v1/pet/{id}")
    fun updatePet (@Path("id") id: String ,@Body petInfo: SavePetResource): Call<com.example.petcareavance.api.dataclasses.pets.PetResponse>

    @GET("api/v1/reserva/byservice/{serviceId}")
    fun getReservaByPaymentId(@Path("serviceId") userId: String): Call<List<ReservaResponse>>

    @GET("api/v1/reserva/{reservaId}")
    fun getReservaById(@Path("reservaId") userId: String): Call<ReservaResponse>

    @POST("api/v1/reserva")
    fun postReserva(@Body saveReserva: SaveReservaResource): Call<ReservaResponse>

    @POST("api/v1/reviews")
    fun postReview(@Body reviewResponse: ReviewResponse): Call<ReviewResponse>

    @GET("api/v1/reviews/byService/{serviceId}")
    fun getReviewByService(@Path("serviceId") userId: String): Call<List<ReviewResponse>>

}

data class UserInfo(val mail: String, val password: String)

data class UserSignInInfo(val mail: String, val password: String, val firstName: String,
                          val lastName: String,val phone: Int, val typeUserId: Int, val dni: Int )
data class UserResponse(val id: Int, val token: String)
data class UserResponseForSingUp(val message: String)

data class PetResponse(val id: Int, val name: String)

