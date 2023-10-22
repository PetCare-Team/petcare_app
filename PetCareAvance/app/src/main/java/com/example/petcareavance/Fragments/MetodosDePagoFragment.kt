package com.example.petcareavance.Fragments
import android.widget.ImageView // Asegúrate de importar ImageView
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
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

interface DeletePaymentCallback {
    fun onDeletePayment(position: Int)
}


class MetodosDePagoFragment : Fragment(), DeletePaymentCallback {

    private lateinit var apiService: ApiService
    private lateinit var paymentMethodAdapter: PaymentMethodAdapter
    private var paymentMethods: List<PaymentResponse> = emptyList()  // Declarar como variable miembro

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_metodos_de_pago, container, false)


        val btnAgregarTarjetaLink = view.findViewById<TextView>(R.id.tvAgregarTarjetaLink)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardar)  // Asegúrate de tener el id correcto


        btnGuardar.setOnClickListener {
            val selectedItemIndex = paymentMethodAdapter.selectedItemIndex
            if (selectedItemIndex != -1) {
                val selectedPaymentMethodId = paymentMethods[selectedItemIndex].id  // Obtener el ID de la tarjeta seleccionada

                val sharedPreferences = requireActivity().getSharedPreferences("SelectedPayment", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("SelectedPaymentId", selectedPaymentMethodId)
                editor.apply()

                Toast.makeText(requireContext(), "ID de pago guardado: $selectedPaymentMethodId", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "No se seleccionó ningún método de pago", Toast.LENGTH_SHORT).show()
            }
        }

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
        paymentMethodAdapter = PaymentMethodAdapter(emptyList(), this)
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


                    paymentMethods = response.body() ?: emptyList()  // Llenar la lista con datos de la API
                    paymentMethodAdapter.paymentMethods = paymentMethods
                    paymentMethodAdapter.notifyDataSetChanged()
                    setSelectionFromSharedPreferences()  // Llama a este método después de cargar los datos

                }
            }


            override fun onFailure(call: Call<List<PaymentResponse>>, t: Throwable) {
                Log.d("ERROR", "ERROR: $t.localizedMessage")
                Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })

        return view
    }
    private fun setSelectionFromSharedPreferences() {
        val sharedPreferences = requireActivity().getSharedPreferences("SelectedPayment", Context.MODE_PRIVATE)
        val selectedPaymentMethodId = sharedPreferences.getInt("SelectedPaymentId", -1)

        if (selectedPaymentMethodId != -1) {
            val selectedItemIndex = paymentMethods.indexOfFirst { it.id == selectedPaymentMethodId }
            if (selectedItemIndex != -1) {
                paymentMethodAdapter.selectedItemIndex = selectedItemIndex
                paymentMethodAdapter.notifyDataSetChanged()
            }
        }
    }




    class PaymentMethodViewHolder(view: View, private val callback: MetodosDePagoFragment, private val adapter: PaymentMethodAdapter) : RecyclerView.ViewHolder(view) {
        val nameCardTextView: TextView = view.findViewById(R.id.cardInfo)
        val deleteIconPayment: ImageView = view.findViewById(R.id.deleteIconPayment)

        val switch: Switch = view.findViewById(R.id.switch4)

        init {
            deleteIconPayment.setOnClickListener {
                callback.onDeletePayment(adapterPosition)
            }
            switch.setOnClickListener {
                val previousSelectedItemIndex = adapter.selectedItemIndex
                if (previousSelectedItemIndex == adapterPosition) {
                    // El usuario tocó el Switch que ya estaba seleccionado, deseleccionarlo
                    switch.isChecked = false
                    adapter.selectedItemIndex = -1
                } else {
                    // El usuario tocó un Switch diferente, seleccionarlo
                    switch.isChecked = true
                    adapter.selectedItemIndex = adapterPosition
                }
                Handler(Looper.getMainLooper()).post {
                    if (previousSelectedItemIndex != -1 && previousSelectedItemIndex != adapterPosition) {
                        // Uncheck the previous item
                        adapter.notifyItemChanged(previousSelectedItemIndex)
                    }
                    adapter.notifyItemChanged(adapterPosition)
                }
            }
        }
        // ... otras vistas
    }
    interface DeletePaymentCallback {
        fun onDeletePayment(position: Int)
    }
    override fun onDeletePayment(position: Int) {
        if (position in paymentMethods.indices) {  // Verificar si la posición es válida
            val paymentMethodId = paymentMethods[position].id.toString()  // Convertir el ID a String
            val sharedPreferences = requireActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("Token", "") ?: ""

            // Realizar la llamada a la API para eliminar
            val call2 = apiService.deletePayment( paymentMethodId)
            call2.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // Remover el método de pago de la lista y notificar al adaptador
                        paymentMethods = paymentMethods.filter { it.id != paymentMethodId.toInt() }
                        paymentMethodAdapter.paymentMethods = paymentMethods
                        paymentMethodAdapter.notifyDataSetChanged()
                        Toast.makeText(requireContext(), "Método de pago eliminado", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "N/A"
                        Log.d("API_ERROR", "Error: $errorBody")
                        Toast.makeText(requireContext(), "Error al eliminar  ${paymentMethodId}  ==== $paymentMethods ", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            })



            // Código para manejar la eliminación del pago
        } else {
            Log.e("ERROR", "Posición inválida: $position")
        }
    }


    inner class PaymentMethodAdapter(
        var paymentMethods: List<PaymentResponse>,
        private val deletePaymentCallback: MetodosDePagoFragment
    ) : RecyclerView.Adapter<PaymentMethodViewHolder>() {
        var selectedItemIndex = -1


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_payment_method, parent, false)
            return PaymentMethodViewHolder(view, deletePaymentCallback, this)
        }

        override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
            val paymentMethod = paymentMethods[position]
            val lastFourDigits = paymentMethod.number.substring(paymentMethod.number.length - 4)

            holder.nameCardTextView.text = "Debit *${lastFourDigits} \n ${paymentMethod.name} ${paymentMethod.lastName}  "
            holder.switch.isChecked = position == selectedItemIndex

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
