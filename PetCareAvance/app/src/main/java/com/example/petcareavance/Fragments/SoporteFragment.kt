package com.example.petcareavance.Fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petcareavance.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class SoporteFragment : Fragment() {



    interface ApiService {
        @GET("users/{id}")
        fun getUserById( @Path("id") id: Int): Call<User>
    }


    data class User(val id:Int,
                    val firstName: String,
                    val lastName:String,
                    val phone:Int,
                    val dni: Int,
                    val mail: String,
    )

    private fun getMyData(view: View) {

        val id: Int
        val sharedPreferences = requireActivity().getSharedPreferences("UserID", MODE_PRIVATE)
        val valor = sharedPreferences.getString("ID", "")

        id = valor!!.toInt()

        Toast.makeText(requireContext(), "${id} ", Toast.LENGTH_LONG).show()

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiService::class.java)

        val retrofitData = retrofitBuilder.getUserById(id)

        retrofitData.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val tvNamePerfil = view.findViewById<TextView>(R.id.tvNamePerfil)
                        tvNamePerfil.text = responseBody.firstName.toString()
                    } else {
                        Toast.makeText(requireContext(), "Error: ", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // Manejar el caso en el que la respuesta del servidor no fue exitosa
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Manejar los errores de la solicitud
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }





override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_soporte, container, false)

        val btnAvanzar = view.findViewById<TextView>(R.id.textView3)

        getMyData(view)

        btnAvanzar.setOnClickListener {
            avanzar()
        }

        return view
    }

    private fun avanzar() {
        val editPerfilFragment = EditPerfil()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, editPerfilFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        const val BASE_URL = "https://petcarebackend.azurewebsites.net/api/v1/"
    }
}
