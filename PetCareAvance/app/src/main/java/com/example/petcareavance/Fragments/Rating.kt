package com.example.petcareavance.Fragments

import android.util.Log

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.petcareavance.R
import com.example.petcareavance.api.dataclasses.review.ReviewResponse
import com.example.petcareavance.api.dataclasses.users.UserResponse2
import com.example.petcareavance.api.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Rating: Fragment() {


    lateinit var apiService: ApiService

    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rating, container, false)
        val myParam: Int? = arguments?.getInt("myParamKey")

        val btnAvanzar = view.findViewById<Button>(R.id.btnService4)

        btnAvanzar.setOnClickListener {
            avanzar()
        }
        val btnRetroceder = view.findViewById<ImageView>(R.id.imageView13)

        btnRetroceder.setOnClickListener {
            retroceder()
        }


        val btnPublicarReview = view.findViewById<Button>(R.id.btnService4)
        btnPublicarReview.setOnClickListener {
            if (myParam != null) {
                publicarReview(myParam)
            }
        }
        return view;
    }

    private fun publicarReview(myParam: Int ){

        // Obtener User ID de SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val myUserId = sharedPreferences.getString("ID", "") ?: ""

        // Obtener Token de SharedPreferences
        val sharedPreferences2 = requireActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE)
        val token = sharedPreferences2.getString("Token", "") ?: ""

        val serviceId = myParam
        val userId = myUserId.toInt()
        val stars = view?.findViewById<RatingBar>(R.id.ratingBar)?.rating?.toInt()
        val description = view?.findViewById<EditText>(R.id.tmDescription3)?.text.toString() //




        val userUpdate = ReviewResponse(
            serviceId = serviceId,
            userId = userId,
            stars = stars?:0,
            description = description,
        )
        // Inicializar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://petcarebackend.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()




        apiService = retrofit.create(ApiService::class.java)
        Log.d("PRUEBA", "$userUpdate")

        val call = apiService.postReview(userUpdate)
        call.enqueue(object : Callback<ReviewResponse> {
            override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                if (response.isSuccessful) {
                    Log.e("Pubicado", "Pubicado")

                } else {
                    val error = response.errorBody()?.string() ?: "Unknown error"
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                    Log.e("Pubicado", "$error")
                }

            }

            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                context?.let {
                    Toast.makeText(it, "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
                    Log.e("Pubicado", "${t.localizedMessage}")

                }
            }
        })

        avanzar()

    }


        private fun avanzar(){
        val serviceInfo = confirmReclamo()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, serviceInfo)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun retroceder(){
        parentFragmentManager.popBackStack()

//        requireFragmentManager().popBackStack()

//        val serviceInfo = MyServices()
//        val transaction = requireFragmentManager().beginTransaction()
//        transaction.replace(R.id.fragment_container, serviceInfo)
//        transaction.addToBackStack(null)
//        transaction.commit()
    }
}