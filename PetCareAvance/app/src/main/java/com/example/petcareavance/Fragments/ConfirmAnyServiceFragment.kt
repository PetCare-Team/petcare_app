package com.example.petcareavance.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.petcareavance.EditDateFragment
import com.example.petcareavance.Fragments.confirmservice.PetFragmentVariation
import com.example.petcareavance.R
import com.example.petcareavance.api.RetrofitClient
import com.example.petcareavance.api.dataclasses.payment.PaymentResponse
import com.example.petcareavance.api.dataclasses.pets.PetResponse
import com.example.petcareavance.api.dataclasses.reservas.ReservaResponse
import com.example.petcareavance.api.dataclasses.reservas.SaveReservaResource
import com.example.petcareavance.api.dataclasses.review.ReviewResource
import com.example.petcareavance.api.dataclasses.review.ReviewResponse
import com.example.petcareavance.api.dataclasses.services.ServiceResponse
import com.example.petcareavance.api.services.ApiService
import com.example.petcareavance.views.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfirmAnyServiceFragment: Fragment() {

    private lateinit var sharedViewModel: SharedViewModel

    lateinit var apiService: ApiService

    companion object {
        const val ARG_SERVICE_ID = "serviceId"
    }


    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_confirmservice, container, false)

        val serviceId = arguments?.getString(ARG_SERVICE_ID, "-1")!!
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val selectedDate = sharedViewModel.selectedDate
        val selectedHour = sharedViewModel.selectedHour

        var seleccionartarjeta: TextView = view.findViewById(R.id.textView59)
        var seleccionartarjetaNombre: TextView = view.findViewById(R.id.textView60)
        val fecha: TextView = view.findViewById(R.id.tvDateSelect)
        val editarPet: TextView = view.findViewById<TextView>(R.id.textView54) // EDITAR PET
        val editarFecha: TextView = view.findViewById<TextView>(R.id.textView55) // EDITAR FECHA
        val hora: TextView = view.findViewById<TextView>(R.id.tvHour)

        // Obtener User ID de SharedPreferences
        val sharedPreferences =
            requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("ID", "") ?: ""

        // Obtener Token de SharedPreferences
        val sharedPreferences2 =
            requireActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE)
        val token = sharedPreferences2.getString("Token", "") ?: ""

        // Obtener id del pet
        val sharedPref =
            requireActivity().getSharedPreferences("PetPreferences", Context.MODE_PRIVATE)
        val petId = sharedPref.getInt(
            "PetId",
            -1
        ) // Usa -1 como valor por defecto en caso de que PetId no exista.

        //Obetener el id de la tarjeta
        val sharedPrefForCardId =
            requireActivity().getSharedPreferences("SelectedPayment", Context.MODE_PRIVATE)
        val tarjetaId = sharedPrefForCardId.getInt(
            "SelectedPaymentId",
            -1
        ) // Usa -1 como valor por defecto en caso de que PetId no exista.





        if (selectedDate != null) {
            fecha.setText(selectedDate)
        } else {

            fecha.setText("introducir fecha")
        }

        seleccionartarjeta.setOnClickListener {
            navigateToSeleccionarTarjetaFragment()
        }
        editarPet.setOnClickListener {
            navigateToPetFragment()
        }
        editarFecha.setOnClickListener {

            navigateToEditDateFragment()
        }

        if (selectedHour != null) {
            hora.setText(selectedHour)
            val splitHour = selectedHour!!.split(":")

            // Asegurarse de que se obtuvieron 2 partes (mes y año)
            if (splitHour.size == 2) {
                val hour = splitHour[0]
                val minute = splitHour[1]

                sharedViewModel.selectedHour = "2023-10-19T${hour}:${minute}:43.764Z"
            } else {
                println("Formato de Hora incorrecto")
            }

            Toast.makeText(requireContext(), "${sharedViewModel.selectedHour}", Toast.LENGTH_LONG)
                .show()

        } else {

            hora.setText("introducir hora")
        }





        val btnRetroceder = view.findViewById<ImageView>(R.id.imageView20)

        btnRetroceder.setOnClickListener {
            retroceder()
        }



        // Primero, encuentra el botón RESERVAR y el RadioGroup en tu layout
        val buttonReservar = view.findViewById<Button>(R.id.btnreservarservice)
        val radioGroupServiceType = view.findViewById<RadioGroup>(R.id.rgServiceType)

        // Establece un OnClickListener en el botón
        buttonReservar.setOnClickListener {



          reservar(view,serviceId)


        }

        var call3 = RetrofitClient.instance.getPaymentByUser(token, userId)

        var userCardItems: List<PaymentResponse>? = null


        call3.enqueue(object : Callback<List<PaymentResponse>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<List<PaymentResponse>>,
                response: Response<List<PaymentResponse>>
            ) {
                userCardItems = response.body()
                val view = view ?: return

                if (userCardItems != null) {

                    val amscotaValue:TextView = view.findViewById(R.id.tvMascotaSelect)

                    if(petId!= null && petId != -1 ){
                        val matchingCard = userCardItems!!.find { it.id == tarjetaId }
                        if (matchingCard != null) {
                            // Si encontramos una mascota con el ID, establecemos su nombre
                            seleccionartarjeta.text = "Debito *${matchingCard.number.takeLast(4)}"
                            seleccionartarjetaNombre.text = "${matchingCard.name}"
//                            amscotaValue.text = matchingCard.name
                        } else {
                            // Manejar la situación en la que no hay ninguna mascota con ese ID
                            amscotaValue.text = "Elige tu mascota"
                        }

                    }



                } else {
                    Toast.makeText(requireContext(), "No se pudo obtener el servicio", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<PaymentResponse>>, t: Throwable) {
                Log.d("asd", "${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        val call2 = RetrofitClient.instance.getPetByUser(token, userId) // Colocar id pro params


        var petDataItems: List<PetResponse>? = null

        call2.enqueue(object : Callback<List<PetResponse>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<List<PetResponse>>,
                response: Response<List<PetResponse>>
            ) {
                petDataItems = response.body()
                val view = view ?: return

                if (petDataItems != null) {
                    Log.d("WWWW", "$petDataItems")

                    val amscotaValue:TextView = view.findViewById(R.id.tvMascotaSelect)

                    if(petId!= null && petId != -1 ){
                        val matchingPet = petDataItems!!.find { it.id == petId }
                        if (matchingPet != null) {
                            // Si encontramos una mascota con el ID, establecemos su nombre
                            amscotaValue.text = matchingPet.name
                        } else {
                            // Manejar la situación en la que no hay ninguna mascota con ese ID
                            amscotaValue.text = "Elige tu mascota"
                        }

                    }



                } else {
                    Toast.makeText(requireContext(), "No se pudo obtener el servicio", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<PetResponse>>, t: Throwable) {
                Log.d("asd", "${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })






        var precio=0.00
        var cuota=0.00
        val cuotaText= view.findViewById<TextView>(R.id.tvCuota)


        var serviceDataItem: ServiceResponse? = null

        val call = RetrofitClient.instance.getServiceById(serviceId) // Colocar id pro params

        call.enqueue(object : Callback<ServiceResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ServiceResponse>,
                response: Response<ServiceResponse>
            ) {
                serviceDataItem = response.body()
                val view = view ?: return

                if (serviceDataItem != null) {
//                    Toast.makeText(requireContext(), "Nombre del servicio: ${serviceId}", Toast.LENGTH_LONG).show()

                    val servicelocation: TextView = view.findViewById(R.id.tvLocation)
                    val servicename: TextView = view.findViewById(R.id.tvNameService)
                    val serviceprice: TextView = view.findViewById(R.id.serviceprice)

                    servicename.text = "${serviceDataItem!!.user.firstName}"
                    servicelocation.text = "Vivo en ${serviceDataItem!!.location}"

                    serviceprice.text = "S/${serviceDataItem!!.price}.00 por dia"
                    precio=serviceDataItem!!.price.toDouble()

                    cuota=(precio*0.2).toDouble()
                    cuotaText.setText("S/${cuota}")

                } else {
                    Toast.makeText(requireContext(), "No se pudo obtener el servicio", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
                Log.d("asd", "${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        val radioGroup = view.findViewById<RadioGroup>(R.id.rgServiceType)
        val adicional= view.findViewById<TextView>(R.id.tvAdicional)
        val total= view.findViewById<TextView>(R.id.tvTotal)


        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbPremium-> {

                    adicional.setText("S/20.00")
                    total.setText("S/"+(cuota+20+precio))

                }
                R.id.rbBasic -> {

                    adicional.setText("S/0.00")
                    total.setText("S/"+(cuota+precio))
                }

            }
        }



        return view;
    }


    private fun retroceder(){
        parentFragmentManager.popBackStack()


    }
    private fun navigateToPetFragment() {
        val petFragmentVariation = PetFragmentVariation()
        // Assuming you're using a `FrameLayout` with the id `fragment_container` to swap out your fragments
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, petFragmentVariation)
            .addToBackStack(null) // Add transaction to the back stack if you want the back button to return to the previous fragment
            .commit()
    }

    private fun navigateToSeleccionarTarjetaFragment(){
        val metodosDePagoFragment = MetodosDePagoFragment()
        // Assuming you're using a `FrameLayout` with the id `fragment_container` to swap out your fragments
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, metodosDePagoFragment)
            .addToBackStack(null) // Add transaction to the back stack if you want the back button to return to the previous fragment
            .commit()
    }

    private fun navigateToEditDateFragment() {
        val editDateFragmentVariation = EditDateFragment()
        // Assuming you're using a `FrameLayout` with the id `fragment_container` to swap out your fragments
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, editDateFragmentVariation)
            .addToBackStack(null) // Add transaction to the back stack if you want the back button to return to the previous fragment
            .commit()
    }

    private fun reservar(view: View ,serviceId:String) {


        // Obtener User ID de SharedPreferences
        val sharedPreferences =
            requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("ID", "") ?: ""

        // Obtener Token de SharedPreferences
        val sharedPreferences2 =
            requireActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE)
        val token = sharedPreferences2.getString("Token", "") ?: ""

        // Obtener id del pet
        val sharedPref =
            requireActivity().getSharedPreferences("PetPreferences", Context.MODE_PRIVATE)
        val petId = sharedPref.getInt(
            "PetId",
            -1
        ) // Usa -1 como valor por defecto en caso de que PetId no exista.

        //Obetener el id de la tarjeta
        val sharedPrefForCardId =
            requireActivity().getSharedPreferences("SelectedPayment", Context.MODE_PRIVATE)
        val tarjetaId = sharedPrefForCardId.getInt(
            "SelectedPaymentId",
            -1
        ) // Usa -1 como valor por defecto en caso de que PetId no exista.


        // Primero, encuentra el botón RESERVAR y el RadioGroup en tu layout
        val buttonReservar = view.findViewById<Button>(R.id.btnreservarservice)
        val radioGroupServiceType = view.findViewById<RadioGroup>(R.id.rgServiceType)

        val reservaResource = SaveReservaResource(

            date = sharedViewModel.selectedDate!!,
            endHour = sharedViewModel.selectedHour!!,
            startHour = sharedViewModel.selectedHour!!,
            estadoId = 2,
            serviceProviderId = serviceId.toInt(),
            clientPaymentId = tarjetaId,

            )


        // Inicializar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://petcarebackend.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()




        apiService = retrofit.create(ApiService::class.java)


        val call = apiService.postReserva(reservaResource)
        call.enqueue(object : Callback<ReservaResponse> {
            override fun onResponse(
                call: Call<ReservaResponse>,
                response: Response<ReservaResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Reserva confirmada con éxito", Toast.LENGTH_LONG).show()


                } else {
                    val error = response.errorBody()?.string() ?: "Unknown error"
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                    Log.e("Pubicado", "$error")
                }

            }

            override fun onFailure(call: Call<ReservaResponse>, t: Throwable) {
                context?.let {
                    Toast.makeText(it, "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
                    Log.e("Pubicado", "${t.localizedMessage}")

                }
            }
        })
    }


}