package com.example.petcareavance.api.services

import com.example.petcareavance.api.dataclasses.payment.AddPaymentResponse
import com.example.petcareavance.api.dataclasses.payment.PaymentResponse
import com.example.petcareavance.api.dataclasses.services.ServiceResponse
import com.example.petcareavance.api.dataclasses.pets.PetResponse
import com.example.petcareavance.api.dataclasses.pets.SavePetResource
import com.example.petcareavance.api.dataclasses.reservas.ReservaResponse
import com.example.petcareavance.api.dataclasses.reservas.SaveReservaResource
import com.example.petcareavance.api.dataclasses.review.ReviewResource
import com.example.petcareavance.api.dataclasses.review.ReviewResponse
import com.example.petcareavance.api.dataclasses.review.SaveReviewResource
import com.example.petcareavance.api.dataclasses.users.UserInfo
import com.example.petcareavance.api.dataclasses.users.UserResponse
import com.example.petcareavance.api.dataclasses.users.UserResponse2
import com.example.petcareavance.api.dataclasses.users.UserResponseForSingUp
import com.example.petcareavance.api.dataclasses.users.UserSignInInfo
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


    @GET("api/v1/services/{id}")
    fun getServiceById(@Path("id") id:String): Call<ServiceResponse>

    @POST
    fun postService(@Body saveReserva: SaveReservaResource): Call<ReservaResponse>

    @POST("api/v1/payment")
    fun postPayment(@Body addPaymentResponse: AddPaymentResponse
    ): Call<AddPaymentResponse>

    @DELETE("api/v1/payment/{id}")
    fun deletePayment(@Path("id") id: String): Call<Void>


    @GET("api/v1/user/{userId}/payment")
    fun getPaymentByUser(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Call<List<PaymentResponse>>

    @GET("api/v1/pet/{id}")
    fun getPetById(@Path("id") id: Int): Call<PetResponse>
    @PUT("api/v1/pet/{id}")
    fun UpdatePet (@Path("id") id: Int ,@Body petInfo: SavePetResource): Call<SavePetResource>

    @GET("api/v1/reserva/byservice/{serviceId}")
    fun getReservaByPaymentId(@Path("serviceId") userId: String): Call<List<ReservaResponse>>

    @GET("api/v1/reserva/{reservaId}")
    fun getReservaById(@Path("reservaId") userId: String): Call<ReservaResponse>

    @POST("api/v1/reviews")
    fun postReview(@Body reviewResponse: ReviewResource): Call<ReviewResponse>

    @GET("api/v1/reviews")
    fun getReviews(): Call<List<ReviewResponse>>

    @GET("api/v1/reviews/byuser/{userId}")
    fun getReview(@Header("Authorization") token: String, @Path("userId") userId: String ): Call<List<ReviewResponse>>

    @PUT("api/v1/reviews/{id}")
    fun UpdateReview (@Path("id") id: Int ,@Body petInfo: SaveReviewResource): Call<SaveReviewResource>

    @GET("api/v1/reviews/byService/{serviceId}")
    fun getReviewByService(@Path("serviceId") userId: String): Call<List<ReviewResponse>>

}