package com.example.petcareavance.Fragments

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petcareavance.R
import com.example.petcareavance.api.dataclasses.payment.PaymentResponse
import com.example.petcareavance.api.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MetodosDePagoFragment : Fragment() {

    private lateinit var apiService: ApiService
    private lateinit var paymentMethodAdapter: PaymentMethodAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_metodos_de_pago, container, false)


        val btnAgregarTarjetaLink = view.findViewById<TextView>(R.id.tvAgregarTarjetaLink)
//
        btnAgregarTarjetaLink.setOnClickListener {
            agregarTarjetaLink()
        }

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

        // Configurar el RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.addItemDecoration(SpacingItemDecoration(0))  // 8px de espacio
        paymentMethodAdapter = PaymentMethodAdapter(emptyList())
        recyclerView.adapter = paymentMethodAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Realizar la llamada a la API
        val call = apiService.getPaymentByUser("Bearer " + token, userId)
        call.enqueue(object : Callback<List<PaymentResponse>> {
            override fun onResponse(
                call: Call<List<PaymentResponse>>,
                response: Response<List<PaymentResponse>>
            ) {
                if (response.isSuccessful) {
                    val paymentMethods = response.body()

                    if (paymentMethods != null) {
                        paymentMethodAdapter.paymentMethods = paymentMethods
                        paymentMethodAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<PaymentResponse>>, t: Throwable) {
                Log.d("ERROR", "ERROR: $t.localizedMessage")
                Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })

        return view
    }

    class PaymentMethodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameCardTextView: TextView = view.findViewById(R.id.cardInfo)
        // ... otras vistas
    }

    inner class PaymentMethodAdapter(
        var paymentMethods: List<PaymentResponse>
    ) : RecyclerView.Adapter<PaymentMethodViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_payment_method, parent, false)
            return PaymentMethodViewHolder(view)
        }

        override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
            val paymentMethod = paymentMethods[position]
            val lastFourDigits = paymentMethod.number.substring(paymentMethod.number.length - 4)

            holder.nameCardTextView.text = "Debit *${lastFourDigits} \n ${paymentMethod.name} ${paymentMethod.lastName}  "
            // ... configurar otras vistas
        }

        override fun getItemCount() = paymentMethods.size
    }

    class SpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            if (position != RecyclerView.NO_POSITION) {
                outRect.top = spacing
                outRect.bottom = spacing
            }
        }
    }


    fun agregarTarjetaLink (){
        Log.d("MetodosDePagoFragment", "agregarTarjetaLink called")

        val agregarMetodosDePago = AgregarMetodoDePagoFragment()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, agregarMetodosDePago)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
