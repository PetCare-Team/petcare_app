package com.example.petcareavance.Fragments

import android.app.Service
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petcareavance.R
import com.example.petcareavance.api.dataclasses.services.ServiceDataItem
import com.example.petcareavance.api.RetrofitClient
import com.example.petcareavance.api.dataclasses.reservas.ReservaResponse
import com.example.petcareavance.api.dataclasses.services.ServiceResponse

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ServicesDataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_data_services, container, false)
        val rvListServices = view.findViewById<RecyclerView>(R.id.rvListServices)

        // Obtener el FragmentManager
        val transaction = requireFragmentManager()

        // Obtener el Token de SharedPreferences
        val sharedPreferences =
            requireActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("Token", "") ?: ""

        // Obtener User ID de SharedPreferences
        val sharedPreferences2 = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val userId = sharedPreferences2.getString("ID", "") ?: ""

        Log.d("ID", userId)

        // Realizar la llamada a la API para obtener la lista de servicios
        val call = RetrofitClient.instance.getReservaByPaymentId(userId)

        val btnAvanzar = view.findViewById<Button>(R.id.button3)

        btnAvanzar.setOnClickListener {
            avanzar()
        }


        val btnretroceder = view.findViewById<ImageView>(R.id.imageView16)

        btnretroceder.setOnClickListener {
            retroceder()
        }

        call.enqueue(object : Callback<List<ReservaResponse>> {
            override fun onResponse(
                call: Call<List<ReservaResponse>>,
                response: Response<List<ReservaResponse>>
            ) {
                val serviceDataItems = response.body()

                Log.d("API_Response", "Received service data: ${serviceDataItems}")



                if (serviceDataItems != null) {
                    Toast.makeText(requireContext(), "Nombre del servicio: ${serviceDataItems}", Toast.LENGTH_LONG).show()

                    // Crear y configurar el adaptador
                    val adapter = ServiceAdapter(serviceDataItems, transaction)

                    // Asignar el adaptador al RecyclerView
                    rvListServices.layoutManager = GridLayoutManager(requireContext(), 1) // Esto supone que quieres 1 columna, cambia el valor si necesitas más columnas
                    rvListServices.adapter = adapter
                } else {
                    Toast.makeText(requireContext(), "No se pudo obtener el servicio", Toast.LENGTH_LONG).show()
                }
            }



            override fun onFailure(call: Call<List<ReservaResponse>>, t: Throwable) {
                Log.d("asd","${t.message}")

                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        return view
    }
    private fun avanzar() {

        val serviceInfo = MyServices()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, serviceInfo)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun retroceder() {

        val serviceInfo = ServicesDataFragment()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, serviceInfo)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    class ServiceAdapter(
        private val serviceList: List<ReservaResponse>,
        private val transaction: FragmentManager
    ) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

        class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val serviceBtn: ImageButton = itemView.findViewById(R.id.serviceBtn)
            val serviceStatus: TextView = itemView.findViewById(R.id.status)
            val serviceName: TextView = itemView.findViewById(R.id.userName)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_services, parent, false)
            return ServiceViewHolder(view)
        }

        fun formatStartHour(startHour: String): String {
            // Paso 1: Parsear la cadena a LocalDateTime
            val dateTime = LocalDateTime.parse(startHour, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

            // Paso 2: Formatear el objeto LocalDateTime a la cadena deseada
            val formatter = DateTimeFormatter.ofPattern("dd/MM")
            return dateTime.format(formatter)
        }

        fun getStatus (statusId: Int): String {
            if(statusId === 1 ){
                return "Pendiente"
            }else if(statusId === 2 ){
                return "En progreso"
            }else if(statusId === 3 ){
                return "Cancelado"
            }
            return "Pendiente"
        }


        override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
            val reserva = serviceList[position]
            Log.d("service.serviceProvider.UserResponse2", "${reserva.serviceProvider}")
            Log.d("service.serviceProvider.UserResponse2", "${reserva.serviceProvider.user}")
            holder.serviceName.text = reserva.serviceProvider.user.firstName

            holder.serviceStatus.text =  getStatus(reserva.estadoId) + "   " + formatStartHour(reserva.startHour)
            holder.serviceName.setOnClickListener {
                Log.d("IMPORTANTE:", "Valoir del id reserva ${reserva.id}")
                avanzarUsuario(reserva.id)
            }
        }

        override fun getItemCount() = serviceList.size

        private fun avanzarUsuario(reservaId: Int) {
            val userDescriptionFragment = MyServices.newInstance(reservaId)
            // Si necesitas pasar más datos, simplemente añade más claves y valores al Bundle en `newInstance()` y recupéralos en tu fragmento.

            val transaction = transaction.beginTransaction()
            transaction.replace(R.id.fragment_container, userDescriptionFragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }
    }
}
