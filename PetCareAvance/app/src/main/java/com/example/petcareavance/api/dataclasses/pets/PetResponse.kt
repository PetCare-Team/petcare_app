package com.example.petcareavance.api.dataclasses.pets

data class PetResponse(val id: Int, val name: String,val description: String,
                       val castrado: Int,
                       val edad: Int,
                       val userId: Int)
