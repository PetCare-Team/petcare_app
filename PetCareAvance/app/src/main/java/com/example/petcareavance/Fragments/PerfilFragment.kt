package com.example.petcareavance.Fragments

import android.util.Log
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.petcareavance.MainActivity
import com.example.petcareavance.R
import com.example.petcareavance.api.RetrofitClient
import com.example.petcareavance.api.dataclasses.users.UserResponse2
import com.example.petcareavance.api.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class   PerfilFragment : Fragment() {

    lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        // Inicializar Retrofit
        val apiService = RetrofitClient.instance



        val btnAvanzar = view.findViewById<TextView>(R.id.textView3)

        btnAvanzar.setOnClickListener {
            avanzar()
        }

        val imMascotas= view.findViewById<ImageView>(R.id.ivMascotas)

        imMascotas.setOnClickListener{

            avanzarMascota()

        }


        val buttontarjetas = view.findViewById<TextView>(R.id.textView9)
        buttontarjetas.setOnClickListener{
            editarMetodosDePago()
        }

        val imlogout= view.findViewById<ImageView>(R.id.ivLogOut)

        imlogout.setOnClickListener{

            logout()

        }

        // Obtener User ID de SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("ID", "") ?: ""

        // Obtener Token de SharedPreferences
        val sharedPreferences2 = requireActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE)
        val token = sharedPreferences2.getString("Token", "") ?: ""

        // Realizar la llamada a la API
        val call = apiService.getUserProfile( "Bearer " + token , userId)
        Log.d("TEST", "token: $token ,userid: $userId")
        call.enqueue(object : Callback<UserResponse2> {
            override fun onResponse(call: Call<UserResponse2>, response: Response<UserResponse2>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse != null) {
                        Log.d("PerfilFragment", "Response: $userResponse")

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
                Log.d("ERROR", "ERROR: $t.localizedMessage")

                Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })

        return view
    }

    private fun avanzar() {
        val editPerfilFragment = EditPerfil()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, editPerfilFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun avanzarMascota() {
        val mascotaFragment = PetFragment()
        val transaction = requireFragmentManager().beginTransaction()

        transaction.replace(R.id.fragment_container, mascotaFragment)

        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun editarMetodosDePago() {
        val fragmentmetodosdepago = MetodosDePagoFragment()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, fragmentmetodosdepago)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun logout() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }



}