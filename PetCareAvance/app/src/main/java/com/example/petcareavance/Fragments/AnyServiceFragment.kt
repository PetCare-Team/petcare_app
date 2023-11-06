package com.example.petcareavance.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.FragmentManager
import com.example.petcareavance.R
import com.example.petcareavance.api.RetrofitClient
import com.example.petcareavance.api.dataclasses.review.ReviewResponse
import com.example.petcareavance.api.dataclasses.services.ServiceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnyServiceFragment: Fragment() {
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var reviewsList: RecyclerView


    val ARG_SERVICE_ID: String = "0"


    companion object {
        const val ARG_SERVICE_ID = "serviceId"
    }

    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_anyservice, container, false)

        val transaction = requireFragmentManager()
        val btnRetroceder = view.findViewById<ImageView>(R.id.imageView20)

        btnRetroceder.setOnClickListener {
            retroceder()
        }



        reviewAdapter = ReviewAdapter() // Inicialización correcta del adaptador.
        val reviewsRecyclerView: RecyclerView = view.findViewById(R.id.reviews_recycler_view)
        reviewsRecyclerView.layoutManager = LinearLayoutManager(context)
        reviewsRecyclerView.adapter = reviewAdapter
        val btnServiceConfirm = view.findViewById<Button>(R.id.btnreservarservice)

        btnServiceConfirm.setOnClickListener{

            avanzarServicio(ARG_SERVICE_ID,transaction)

        }

         var serviceDataItem: ServiceResponse? = null



        reviewsRecyclerView.adapter = reviewAdapter


        var call: Call<ServiceResponse>? = null
        val serviceId = arguments?.getInt("SERVICE_ID") ?: -1 // -1 como valor por defecto si no se encuentra
        if (serviceId == -1) {
            Toast.makeText(requireContext(), "ID del servicio no encontrado", Toast.LENGTH_LONG).show()
            return view // Salir de la función si no hay ID válido.
        }
        loadReviews(serviceId.toString()) // Llama a este método para cargar las reseñas.
        val servicelocation: TextView = view.findViewById(R.id.servicelocation)
        val servicename: TextView = view.findViewById(R.id.servicename)
        val servicedni: TextView = view.findViewById(R.id.servicedni)
        val servicedescription: TextView = view.findViewById(R.id.servicedescription)
        var servicenameBig: TextView = view.findViewById(R.id.textView36)
       // val serviceprice: TextView = view.findViewById(R.id.tvServicePrice)



        if (serviceId == -1) {
                Toast.makeText(requireContext(), "ID del servicio no encontrado", Toast.LENGTH_LONG).show()
            // Manejar la situación cuando el ID no está presente, tal vez retroceder o mostrar un mensaje.
        } else {
            // Aquí usas el ID para hacer la llamada a la API
            call = RetrofitClient.instance.getServiceById(serviceId.toString()) // Asegúrate de que tu API soporte este método y pasar el ID correctamente.

        }




        if (call != null) {
            call.enqueue(object : Callback<ServiceResponse> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<ServiceResponse>,
                    response: Response<ServiceResponse>
                ) {
                    Log.d("TESTTTTT", "$serviceDataItem")
                    serviceDataItem = response.body()
                    val view = view ?: return

                    if (serviceDataItem != null) {
                        Toast.makeText(requireContext(), "Nombre del servicio: ${serviceDataItem}", Toast.LENGTH_LONG).show()

                        val servicelocation: TextView = view.findViewById(R.id.servicelocation)
                        val servicename: TextView = view.findViewById(R.id.servicename)
                        val servicedni: TextView = view.findViewById(R.id.servicedni)
                        val servicedescription: TextView = view.findViewById(R.id.servicedescription)
                        val serviceprice: TextView = view.findViewById(R.id.serviceprice)
                        var servicenameBig: TextView = view.findViewById(R.id.textView36)

                        servicenameBig.text = "Agregar a la api" // "Sobre ${serviceDataItem!!.user.firstName}"
                        servicename.text = "Agregar a la api" // "Me llamo ${serviceDataItem!!.user.firstName} ${serviceDataItem!!.user.lastName}"
                        servicelocation.text = "Vivo en ${serviceDataItem!!.location}"
                        servicedni.text = "${serviceDataItem!!.dni}"
                        servicedescription.text = "${serviceDataItem!!.description}"
                        serviceprice.text = "S/${serviceDataItem!!.price}.00 por dia"

                    } else {
                        Toast.makeText(requireContext(), "No se pudo obtener el servicio", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
                    Log.d("asd", "${t.message}")
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }




        return view;
    }

    private fun loadReviews(serviceId:String) {
        // Aquí deberías hacer la llamada a tu API para obtener las reseñas
        // Por ejemplo, usando Retrofit puedes hacer algo como esto:
        RetrofitClient.instance.getReviewByService(serviceId).enqueue(object : Callback<List<ReviewResponse>> {
            override fun onResponse(
                call: Call<List<ReviewResponse>>,
                response: Response<List<ReviewResponse>>
            ) {
                if (response.isSuccessful) {
                    // Actualiza la UI con los datos de las reseñas
                    reviewAdapter.setReviews(response.body() ?: emptyList())
                } else {
                    Toast.makeText(requireContext(), "Error: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<ReviewResponse>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })
    }



    private fun retroceder(){
        parentFragmentManager.popBackStack()


    }
}

// Adaptador para el RecyclerView
class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private var reviews: List<ReviewResponse> = emptyList()

    fun setReviews(reviews: List<ReviewResponse>) {
        this.reviews = reviews
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int = reviews.size

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Inicializa las vistas aquí, como el TextView para el nombre de usuario, la imagen, etc.
        private val userName: TextView = itemView.findViewById(R.id.userName)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar2)
        // Más vistas...

        fun bind(review: ReviewResponse) {
            // Asumiendo que tienes un campo `userName` en la respuesta de la API:
            Log.d("review", "$review")
            userName.text = review.userId.toString() // Usa el campo correcto de tu objeto `ReviewResponse`.
            ratingBar.rating = review.stars.toFloat()
            // ...
        }
    }
}

    private fun avanzarServicio(serviceId: String,transaction: FragmentManager) {
        val serviceFragment = ConfirmAnyServiceFragment()
        val bundle = Bundle()
        bundle.putString(serviceId, serviceId)

        serviceFragment.arguments = bundle

        val transaction = transaction.beginTransaction()
        transaction.replace(R.id.fragment_container, serviceFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

