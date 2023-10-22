package com.example.petcareavance

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast

import com.example.petcareavance.api.RetrofitClient
import com.example.petcareavance.api.dataclasses.pets.PetResponse
import com.example.petcareavance.api.dataclasses.pets.SavePetResource
import com.example.petcareavance.api.dataclasses.users.UserResponse2
import com.example.petcareavance.api.dataclasses.users.UserUpdate
import com.example.petcareavance.api.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class editPet : Fragment() {

    companion object {
        const val ARG_PERRO_ID = "perroId"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_edit_pet, container, false)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://petcarebackend.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)


        val etNamePet= view.findViewById<EditText>(R.id.etNamePet)
        val etEdadPet= view.findViewById<EditText>(R.id.etEdadPet)
        val etDescriptionPet= view.findViewById<EditText>(R.id.etDescription)
        val swCastrado= view.findViewById<Switch>(R.id.swCastrado)
        val btsave= view.findViewById<Button>(R.id.btSavePet)

        val id=arguments?.getInt(ARG_PERRO_ID, -1)!!

        val num= 2

        val call = apiService.getPetById(id)
        call.enqueue(object : Callback<PetResponse> {
            override fun onResponse(call: Call<PetResponse>, response: Response<PetResponse>) {
                if (response.isSuccessful) {
                    val petResponse = response.body()
                    if (petResponse != null) {
                        // Actualizar las vistas con los datos obtenidos
                        etNamePet.setText(petResponse.name)
                        etDescriptionPet.setText(petResponse.description)
                        etEdadPet.setText(petResponse.edad.toString())
                        if(petResponse.castrado==0)
                            swCastrado.isChecked=false
                        else swCastrado.isChecked=true

                    }

                    }

            }

            override fun onFailure(call: Call<PetResponse>, t: Throwable) {
                // Manejar el error
                Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })


        btsave.setOnClickListener {
            updatePerfil()
        }



        return view
    }



    private fun updatePerfil( ){
        val Name = view?.findViewById<EditText>(R.id.etNamePet)?.text.toString()
        val Edad = view?.findViewById<EditText>(R.id.etEdadPet)?.text.toString()
        val description = view?.findViewById<EditText>(R.id.etDescription)?.text.toString()
        val castrado = view?.findViewById<Switch>(R.id.swCastrado)?.isChecked!!

        fun convert(b:Boolean): Int {

            if(b==true)
                return 1
            else return 0
        }

        val sharedPreferences = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("ID", "") ?: ""

        val id=arguments?.getInt(ARG_PERRO_ID, -1)!!

        val petUpdate = SavePetResource(

                name=Name,
                description= description,
                castrado= convert(castrado),
                edad= Edad.toInt(),
                userId= userId.toInt()

        )
        // Inicializar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://petcarebackend.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)


        val call = apiService.UpdatePet(id ,petUpdate)
        call.enqueue(object : Callback<SavePetResource> {
            override fun onResponse(call: Call<SavePetResource>, response: Response<SavePetResource>) {
                if (response.isSuccessful) {
                    // La actualización fue exitosa
                    Toast.makeText(requireContext(), "Perfil actualizado exitosamente", Toast.LENGTH_LONG).show()
                } else {
                    // Hubo un error en la actualización
                    Toast.makeText(requireContext(), "Error en la actualización: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<SavePetResource>, t: Throwable) {
                // Manejar el error
                Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })


    }






}


