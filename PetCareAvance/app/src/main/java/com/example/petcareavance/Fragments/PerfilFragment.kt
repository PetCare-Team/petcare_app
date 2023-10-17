package com.example.petcareavance.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.petcareavance.R
import com.example.petcareavance.interfaces.ApiServiceUser
import com.example.petcareavance.interfaces.UserResponse2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class   PerfilFragment : Fragment() {

    lateinit var apiService: ApiServiceUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        // Inicializar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://petcarebackend.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiServiceUser::class.java)

        val btnAvanzar = view.findViewById<TextView>(R.id.textView3)

        btnAvanzar.setOnClickListener {
            avanzar()
        }

        val imMascotas= view.findViewById<ImageView>(R.id.ivMascotas)

        imMascotas.setOnClickListener{

           avanzarMascota()

        }

        // Obtener User ID de SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("ID", "") ?: ""

        // Obtener Token de SharedPreferences
        val sharedPreferences2 = requireActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("Token", "") ?: ""

        // Realizar la llamada a la API
        val call = apiService.getUserProfile( "Bearer " + token , userId)
        call.enqueue(object : Callback<UserResponse2> {
            override fun onResponse(call: Call<UserResponse2>, response: Response<UserResponse2>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse != null) {
                        // Actualizar las vistas con los datos obtenidos
                        view.findViewById<TextView>(R.id.textView8).text = "${userResponse.firstName} ${userResponse.lastName}"
                        view.findViewById<TextView>(R.id.textView4).text = userResponse.dni.toString()
                        view.findViewById<TextView>(R.id.textView5).text = userResponse.phone.toString()
                        view.findViewById<TextView>(R.id.textView6).text = userResponse.mail
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse2>, t: Throwable) {
                // Manejar el error
                Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })

        return view
    }

    private fun avanzar() {
        val editPerfilFragment = PetFragment()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, editPerfilFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun avanzarMascota() {
        val MascotaFragment = PetFragment()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.layout.fragment_perfil ,MascotaFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }




}