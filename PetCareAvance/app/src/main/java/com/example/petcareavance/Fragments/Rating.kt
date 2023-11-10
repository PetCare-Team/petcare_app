package com.example.petcareavance.Fragments

import android.util.Log

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.petcareavance.R
import com.example.petcareavance.api.dataclasses.pets.SavePetResource
import com.example.petcareavance.api.dataclasses.review.ReviewResource
import com.example.petcareavance.api.dataclasses.review.ReviewResponse
import com.example.petcareavance.api.dataclasses.review.SaveReviewResource
import com.example.petcareavance.api.dataclasses.users.UserResponse2
import com.example.petcareavance.api.services.ApiService
import com.example.petcareavance.editPet
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
        var ratingId: Int=0

        val btnAvanzar = view.findViewById<Button>(R.id.btnService4)

        // Obtener User ID de SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("ID", "") ?: ""

        // Obtener Token de SharedPreferences
        val sharedPreferences2 = requireActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE)
        val token = sharedPreferences2.getString("Token", "") ?: ""

        val retrofit = Retrofit.Builder()
            .baseUrl("https://petcarebackend.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()




        apiService = retrofit.create(ApiService::class.java)

        val call = apiService.getReviews()
        call.enqueue(object : Callback<List<ReviewResponse>> {
            override fun onResponse(call: Call<List<ReviewResponse>>, response: Response<List<ReviewResponse>>) {
                if (response.isSuccessful) {
                    val reviewResponse = response.body()

                    if (reviewResponse != null) {
                        for(review in reviewResponse) {
                            if (review.service.serviceId == myParam) {


                                // Actualizar las vistas con los datos obtenidos
                                view.findViewById<RatingBar>(R.id.ratingBar).rating = review.stars.toFloat()
                                view.findViewById<EditText>(R.id.tmDescription3)
                                    .setText(review.description)
                                ratingId=review.reviewId

                            }
                        }

                    }
                }
            }

            override fun onFailure(call: Call<List<ReviewResponse>>, t: Throwable) {
                Log.d("asd","${t.message}")
                // Manejar el error
                Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })

        btnAvanzar.setOnClickListener {
            avanzar()
        }
        val btnRetroceder = view.findViewById<ImageView>(R.id.imageView13)

        btnRetroceder.setOnClickListener {
            retroceder()
        }


        val btnPublicarReview = view.findViewById<Button>(R.id.btnService4)
        btnPublicarReview.setOnClickListener {
            if (ratingId==null) {
                if (myParam != null) {
                    publicarReview(myParam,view)
                }
            }

            else {
                if (myParam != null) {
                    updateReview(myParam,ratingId)
                }

            }
        }
        return view;
    }
    private fun updateReview(myParam: Int, ratingId :Int ){
        val Starts = view?.findViewById<RatingBar>(R.id.ratingBar)?.rating?.toInt()!!
        val description = view?.findViewById<EditText>(R.id.tmDescription3)?.text.toString()




        val sharedPreferences = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("ID", "") ?: ""
        val serviceId = myParam

        val id=ratingId

        val reviewUpdate = SaveReviewResource(

            description = description,
            serviceId= serviceId,
            userId=userId.toInt(),
            stars= Starts

        )
        Log.d("reviewUpdate", "${reviewUpdate.stars}")
        // Inicializar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://petcarebackend.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)


        val call = apiService.UpdateReview(id ,reviewUpdate)
        call.enqueue(object : Callback<SaveReviewResource> {
            override fun onResponse(call: Call<SaveReviewResource>, response: Response<SaveReviewResource>) {
                if (response.isSuccessful) {
                    // La actualización fue exitosa
                    Toast.makeText(requireContext(), "Review actualizado exitosamente", Toast.LENGTH_LONG).show()
                } else {
                    // Hubo un error en la actualización
                    Toast.makeText(requireContext(), "Error en la actualización: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<SaveReviewResource>, t: Throwable) {
                // Manejar el error
                Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })


    }

    private fun publicarReview(myParam: Int,view: View ){

        // Obtener User ID de SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val myUserId = sharedPreferences.getString("ID", "") ?: ""

        // Obtener Token de SharedPreferences
        val sharedPreferences2 = requireActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE)
        val token = sharedPreferences2.getString("Token", "") ?: ""

        val serviceId = myParam
        val userId = myUserId.toInt()
        val stars = view.findViewById<RatingBar>(R.id.ratingBar)?.rating?.toInt()
        val description = view?.findViewById<EditText>(R.id.tmDescription3)?.text.toString() //




        val userUpdate = ReviewResource(

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