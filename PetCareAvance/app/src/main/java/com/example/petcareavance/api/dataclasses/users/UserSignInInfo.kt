package com.example.petcareavance.api.dataclasses.users

data class UserSignInInfo(val mail: String, val password: String, val firstName: String,
                          val lastName: String,val phone: Int, val typeUserId: Int, val dni: Int )
