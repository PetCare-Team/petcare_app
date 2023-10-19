package com.example.petcareavance.api.dataclasses.users

import com.example.petcareavance.api.dataclasses.typeusers.TypeUser

data class UserResponse2(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phone: Int,
    val dni: Int,
    val typeUser: TypeUser,
    val mail: String
)
