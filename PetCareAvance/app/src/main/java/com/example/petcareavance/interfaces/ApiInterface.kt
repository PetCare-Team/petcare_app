package com.example.petcareavance.interfaces

import com.example.petcareavance.ServiceDataItem
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("users")
    fun getData(): Call<List<ServiceDataItem>>
}