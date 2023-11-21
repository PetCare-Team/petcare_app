package com.example.petcareavance.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.petcareavance.R
import com.example.petcareavance.api.dataclasses.users.UserResponse2
import com.example.petcareavance.api.dataclasses.users.UserUpdate
import com.example.petcareavance.api.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditPerfil : Fragment() {

    lateinit var apiService: ApiService


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_perfil, container, false)

        val btnRetroceder = view.findViewById<ImageView>(R.id.imageButton)


        val btnGuardar = view.findViewById<Button>(R.id.button2)

        // Inicializar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://petcarebackend.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Obtener User ID de SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("ID", "") ?: ""

        // Obtener Token de SharedPreferences
        val sharedPreferences2 = requireActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE)
        val token = sharedPreferences2.getString("Token", "") ?: ""

        Log.d("EditPerfil", "UserId: $userId, Token: $token")
        val call = apiService.getUserProfile( "Bearer " + token , userId)
        call.enqueue(object : Callback<UserResponse2> {
            override fun onResponse(call: Call<UserResponse2>, response: Response<UserResponse2>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse != null) {
                        // Actualizar las vistas con los datos obtenidos
                        view.findViewById<EditText>(R.id.tvDirecc3).setText(userResponse.firstName)
                        view.findViewById<EditText>(R.id.tvDirecc4).setText(userResponse.lastName)
                        view.findViewById<EditText>(R.id.tvDirecc5).setText(userResponse.dni.toString())
                        view.findViewById<EditText>(R.id.etPhone).setText (userResponse.phone.toString())
                        view.findViewById<EditText>(R.id.etaddress).setText (userResponse.mail)

                    }
                }
            }

            override fun onFailure(call: Call<UserResponse2>, t: Throwable) {
                // Manejar el error
                Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })


        btnRetroceder.setOnClickListener {
            retroceder()
        }
        btnGuardar.setOnClickListener {
            updatePerfil(userId, token)
        }

        return view
    }

    private fun retroceder() {
        parentFragmentManager.popBackStack()

//            val publicarFragment = SoporteFragment()
//        val transaction = requireFragmentManager().beginTransaction()
//        transaction.replace(R.id.fragment_container, publicarFragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
    }

    private fun updatePerfil( userId :String, token:String){
        val firstName = view?.findViewById<EditText>(R.id.tvDirecc3)?.text.toString()
        val lastName = view?.findViewById<EditText>(R.id.tvDirecc4)?.text.toString()
        val dni = view?.findViewById<EditText>(R.id.tvDirecc5)?.text.toString().toIntOrNull()
        val phone = view?.findViewById<EditText>(R.id.etPhone)?.text.toString().toIntOrNull()
        val mail = view?.findViewById<EditText>(R.id.etaddress)?.text.toString()
        val password = view?.findViewById<EditText>(R.id.etPassword)?.text.toString()
        Log.d("EditPerfil", "UserId: $userId, Token: $token, " +
                "FirstName: $firstName, LastName: $lastName, DNI: $dni," +
                "Phone: $phone, Mail: $mail. Password: $password")

        val userUpdate = UserUpdate(
            mail = mail,
            password = password,
            firstName = firstName,
            lastName = lastName,
            phone = phone ?: 0,  // Asumiendo un valor por defecto de 0 si el valor es null
            typeUserId = 1,  // Asumiendo un valor por defecto
            dni = dni ?: 0  // Asumiendo un valor por defecto de 0 si el valor es null
        )
        // Inicializar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://petcarebackend.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)


        val call2 = apiService.updateUserInfo("Bearer $token", userId, userUpdate)

        call2.enqueue(object : Callback<UserUpdate> {
            override fun onResponse(call: Call<UserUpdate>, response: Response<UserUpdate>) {
                if (response.isSuccessful) {
                    // La actualización fue exitosa
                    Toast.makeText(requireContext(), "Perfil actualizado exitosamente", Toast.LENGTH_LONG).show()
                } else {
                    Log.d("ERRRRRRRRRRROR", "HTTP Status Code: ${response.code()}")
                    Log.d("HTTP Response", "Response: $response")

                    Log.e("ERRRRRRRRRRROR", "Error updating profile: ${response.errorBody()?.string()}")

                    // Hubo un error en la actualización
                    Toast.makeText(requireContext(), "Error en la actualización: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<UserUpdate>, t: Throwable) {
                Log.e("ERRRRRRRRRRROR", "Error updating profile: ${t.localizedMessage}")

                // Manejar el error
                Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })


    }



}