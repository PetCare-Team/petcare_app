package com.example.petcareavance.api.dataclasses.users

data class User(val id:Int,
                val firstName: String,
                val lastName:String,
                val phone:Int,
                val dni: Int,
                val mail: String,
)