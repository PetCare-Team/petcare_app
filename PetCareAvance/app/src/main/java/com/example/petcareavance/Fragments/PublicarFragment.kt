// PublicarFragment.kt

package com.example.petcareavance.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.petcareavance.R
import com.example.petcareavance.api.RetrofitClient
import com.example.petcareavance.api.dataclasses.services.ServicePostRequest
import com.example.petcareavance.api.dataclasses.services.ServiceResponse
import com.example.petcareavance.api.dataclasses.users.UserResponse2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PublicarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_publicar, container, false)

        val spinner = view.findViewById<Spinner>(R.id.spin1)
        val spinner2 = view.findViewById<Spinner>(R.id.snrHour)

        val opciones = listOf("Dias disponible", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
        val opciones2 = listOf("Horas disponible", "2pm", "8pm", "7am")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones2)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner2.adapter = adapter2

        spinner.setSelection(0)
        spinner2.setSelection(0)

        // Obtén una referencia al TextView al que deseas cambiar el color del borde
        val textView = view.findViewById<TextView>(R.id.tvDirecc)
        val textView2 = view.findViewById<TextView>(R.id.tmDescription)

        // Asociar la acción al botón
        val btnIrAProceso = view.findViewById<Button>(R.id.btnService)
        btnIrAProceso.setOnClickListener {
            getUserDataAndPostService(view)
            irAProcesoPublicacion()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnOculto = view.findViewById<Button>(R.id.btnOculto)
        val btnService = view.findViewById<Button>(R.id.btnService)

        btnOculto.isEnabled = false
        btnOculto.visibility = View.GONE

        checkServicesAndToggleButton(btnService, btnOculto)
        loadLastServiceData()
    }

    private fun checkServicesAndToggleButton(btnService: Button, btnOculto: Button) {

        val sharedPreferences = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        // Intenta recuperar un String y conviértelo a Int, manejando el caso en que no sea un número
        val userIdString = sharedPreferences.getString("ID", null)
        val userId = userIdString?.toIntOrNull() ?: -1 // Convierte a Int o usa -1 si no es posible


        if (userId != -1) {
            val apiService = RetrofitClient.instance
            apiService.getServicesByUserId(userId).enqueue(object :
                Callback<List<ServiceResponse>> {
                override fun onResponse(call: Call<List<ServiceResponse>>, response: Response<List<ServiceResponse>>) {
                    if (response.isSuccessful) {
                        val services = response.body()
                        val hasServices = !services.isNullOrEmpty()

                        activity?.runOnUiThread {
                            if (hasServices) {
                                saveLastServiceId()
                                Log.d("ServiceCheck", "Hay servicios disponibles.")
                                btnService.text = getString(R.string.editar)
                                btnOculto.isEnabled = true
                                btnOculto.visibility = View.VISIBLE

                                btnOculto.setOnClickListener {
                                    // Llamamos a la función deleteService que eliminará el servicio y luego actualizaremos la vista
                                    deleteService {
                                        // Actualizamos la vista después de eliminar el servicio
                                        checkServicesAndToggleButton(btnService, btnOculto)
                                    }
                                }

                                btnService.setOnClickListener {
                                    val thisView = view ?: return@setOnClickListener
                                    getUserDataAndUpdateService(thisView)
                                }

                            } else {
                                Log.d("ServiceCheck", "No hay servicios disponibles.")
                                btnService.text = getString(R.string.publicar_servicio)
                                // El botón btnOculto se mantiene deshabilitado y oculto
                            }
                        }
                    } else {
                        Log.e("ServiceCheck", "La respuesta al verificar los servicios no fue exitosa.")
                    }
                }

                override fun onFailure(call: Call<List<ServiceResponse>>, t: Throwable) {
                    Log.e("ServiceCheck", "Error al realizar la llamada a la API.", t)
                }
            })
        } else {
            Log.d("ServiceCheck", "UserID no encontrado en SharedPreferences.")
        }
    }

    private fun loadLastServiceData() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        // Intenta recuperar un String y conviértelo a Int, manejando el caso en que no sea un número
        val userIdString = sharedPreferences.getString("ID", null)
        val userId = userIdString?.toIntOrNull() ?: -1

        Log.d("ServiceLoader", "Cargando servicios para el usuario con ID: $userId")

        if (userId != -1) {
            val apiService = RetrofitClient.instance
            apiService.getServicesByUserId(userId).enqueue(object :
                Callback<List<ServiceResponse>> {
                override fun onResponse(call: Call<List<ServiceResponse>>, response: Response<List<ServiceResponse>>) {
                    if (response.isSuccessful) {
                        val services = response.body()
                        if (!services.isNullOrEmpty()) {
                            val lastService = services.last()
                            Log.d("ServiceLoader", "Último servicio cargado: ${lastService.serviceId}")

                            // Actualiza la interfaz de usuario con la información del último servicio
                            updateUIWithServiceData(lastService)
                        } else {
                            Log.d("ServiceLoader", "El usuario no tiene servicios para cargar")
                        }
                    } else {
                        Log.e("ServiceLoader", "La respuesta al cargar servicios no fue exitosa: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<List<ServiceResponse>>, t: Throwable) {
                    Log.e("ServiceLoader", "Fallo al cargar los servicios: ${t.message}", t)
                }
            })
        } else {
            Log.d("ServiceLoader", "No se encontró UserID en SharedPreferences")
        }
    }


    private fun updateUIWithServiceData(service: ServiceResponse) {
        val textViewLocation = requireView().findViewById<TextView>(R.id.tvDirecc)
        val textViewDescription = requireView().findViewById<TextView>(R.id.tmDescription)
        val switchCuidador = requireView().findViewById<Switch>(R.id.switch1)
        val editTextPrice = requireView().findViewById<EditText>(R.id.editTextNumberDecimal)

        textViewLocation.text = service.location
        textViewDescription.text = service.description
        switchCuidador.isChecked = service.cuidador
        editTextPrice.setText(service.price.toString())
    }

    private fun getUserDataAndPostService(view: View) {
        // Obtener User ID y Token de SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("ID", "") ?: ""
        val sharedPreferences2 = requireActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE)
        val token = sharedPreferences2.getString("Token", "") ?: ""
        val apiService = RetrofitClient.instance

        val call = apiService.getUserProfile( "Bearer " + token , userId)

        // Realizar la llamada a la API para obtener datos del usuario
        apiService.getUserProfile("Bearer $token", userId.toString()).enqueue(object :
            Callback<UserResponse2> {
            override fun onResponse(call: Call<UserResponse2>, response: Response<UserResponse2>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    userResponse?.let {
                        postServiceData(view, it.phone, it.dni,sharedPreferences.getString("ID", "0")?.toInt() ?: 0)
                    }
                } else {
                    // Manejar respuesta no exitosa
                }
            }

            override fun onFailure(call: Call<UserResponse2>, t: Throwable) {
                // Manejar el error
            }
        })
    }

    private fun postServiceData(view: View, phone: Int, dni: Int, userId: Int) {
        val price = view.findViewById<EditText>(R.id.editTextNumberDecimal).text.toString().toIntOrNull() ?: 0
        val description = view.findViewById<EditText>(R.id.tmDescription).text.toString()
        val location = view.findViewById<EditText>(R.id.tvDirecc).text.toString()
        val cuidador = view.findViewById<Switch>(R.id.switch1).isChecked
        val apiService = RetrofitClient.instance

        val servicePostRequest = ServicePostRequest(
            price = price,
            description = description,
            location = location,
            phone = phone,
            dni = dni,
            cuidador = cuidador,
            userId = userId
        )

        // Hacer la solicitud POST al endpoint de tu servicio
        apiService.postService(servicePostRequest).enqueue(object : Callback<ServicePostRequest> {
            override fun onResponse(call: Call<ServicePostRequest>, response: Response<ServicePostRequest>) {
                saveLastServiceId()
            }

            override fun onFailure(call: Call<ServicePostRequest>, t: Throwable) {
                // Manejar el fallo en la solicitud
            }
        })
    }

    private fun saveLastServiceId() {
        val userIdSharedPreferences = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val userIdString = userIdSharedPreferences.getString("ID", null)
        val userId = userIdString?.toIntOrNull() ?: -1

        Log.d("saveLastServiceId", "Recuperando UserID: $userId")

        if (userId == -1) {
            Log.e("saveLastServiceId", "UserID no encontrado.")
            return
        }

        val apiService = RetrofitClient.instance
        apiService.getServicesByUserId(userId).enqueue(object : Callback<List<ServiceResponse>> {
            override fun onResponse(call: Call<List<ServiceResponse>>, response: Response<List<ServiceResponse>>) {
                if (response.isSuccessful) {
                    val services = response.body()
                    if (!services.isNullOrEmpty()) {
                        val lastServiceId = services.last().serviceId
                        Log.d("saveLastServiceId", "Guardando LastServiceId: $lastServiceId")

                        // Usamos un archivo de preferencias que se alinee con el usado en deleteService()
                        val lastServiceSharedPreferences = requireActivity().getSharedPreferences("LastService", Context.MODE_PRIVATE)
                        with(lastServiceSharedPreferences.edit()) {
                            putInt("LastServiceId", lastServiceId)
                            apply()
                        }
                    } else {
                        Log.d("saveLastServiceId", "La lista de servicios está vacía.")
                    }
                } else {
                    Log.e("saveLastServiceId", "Respuesta no exitosa al recuperar servicios.")
                }
            }

            override fun onFailure(call: Call<List<ServiceResponse>>, t: Throwable) {
                Log.e("saveLastServiceId", "Fallo al realizar la solicitud: ${t.message}", t)
            }
        })
    }


    private fun getUserDataAndUpdateService(view: View) {
        // Logging para inicio de la función
        Log.d("UpdateService", "Iniciando getUserDataAndUpdateService")

        val sharedPreferences = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("ID", "") ?: ""
        val sharedPreferences2 = requireActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE)
        val token = sharedPreferences2.getString("Token", "") ?: ""
        val apiService = RetrofitClient.instance

        // Logging del UserID y el Token obtenidos
        Log.d("UpdateService", "UserID: $userId, Token: $token")

        // Realizar la llamada a la API para obtener datos del usuario
        apiService.getUserProfile("Bearer $token", userId).enqueue(object :
            Callback<UserResponse2> {
            override fun onResponse(call: Call<UserResponse2>, response: Response<UserResponse2>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    userResponse?.let {
                        // Logging de los datos de usuario obtenidos
                        Log.d("UpdateService", "Datos de usuario obtenidos: $userResponse")
                        updateServiceData(view, it.phone, it.dni, userId.toIntOrNull() ?: 0)
                    }
                } else {
                    // Logging de respuesta no exitosa
                    Log.e("UpdateService", "Respuesta no exitosa de getUserProfile: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<UserResponse2>, t: Throwable) {
                // Logging del fallo
                Log.e("UpdateService", "Fallo en getUserProfile: ${t.localizedMessage}")
            }
        })
    }

    private fun updateServiceData(view: View, phone: Int, dni: Int, userId: Int) {
        Log.d("UpdateService", "Iniciando updateServiceData con UserID: $userId")

        val price = view.findViewById<EditText>(R.id.editTextNumberDecimal).text.toString().toIntOrNull() ?: 0
        val description = view.findViewById<EditText>(R.id.tmDescription).text.toString()
        val location = view.findViewById<EditText>(R.id.tvDirecc).text.toString()
        val cuidador = view.findViewById<Switch>(R.id.switch1).isChecked
        val apiService = RetrofitClient.instance

        val servicePostRequest = ServicePostRequest(
            price = price,
            description = description,
            location = location,
            phone = phone,
            dni = dni,
            cuidador = cuidador,
            userId = userId
        )

        // Logging de los datos a enviar en la solicitud de actualización
        Log.d("UpdateService", "Datos de la solicitud de actualización: $servicePostRequest")

        val sharedPreferences = requireActivity().getSharedPreferences("LastService", Context.MODE_PRIVATE)
        val serviceId = sharedPreferences.getInt("LastServiceId", -1)

        if (serviceId == -1) {
            // Logging de servicio no encontrado
            Log.e("UpdateService", "No se encontró LastServiceId en SharedPreferences.")
            return
        }

        // Logging del ID de servicio a actualizar
        Log.d("UpdateService", "Actualizando servicio con ID: $serviceId")

        // Hacer la solicitud de actualización al API
        apiService.updateService(serviceId, servicePostRequest).enqueue(object :
            Callback<ServiceResponse> {
            override fun onResponse(call: Call<ServiceResponse>, response: Response<ServiceResponse>) {
                if (response.isSuccessful) {
                    // Logging de respuesta exitosa
                    Log.d("UpdateService", "Servicio actualizado exitosamente.")
                } else {
                    // Logging de respuesta no exitosa
                    Log.e("UpdateService", "Respuesta no exitosa en updateService: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
                // Logging del fallo
                Log.e("UpdateService", "Fallo al actualizar el servicio: ${t.localizedMessage}")
            }
        })
    }


    private fun deleteService(onDeletionComplete: () -> Unit) {

        val sharedPreferences = requireActivity().getSharedPreferences("LastService", Context.MODE_PRIVATE)
        val serviceId = sharedPreferences.getInt("LastServiceId", -1)
        Log.e("deleteService", "${serviceId}")
        if (serviceId == -1) {
            Log.e("deleteService", "No hay servicio para eliminar.")
            return
        }

        val apiService = RetrofitClient.instance
        apiService.deleteService(serviceId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("deleteService", "Servicio eliminado correctamente.")
                    clearLastServiceId()
                    onDeletionComplete()
                } else {
                    Log.e("deleteService", "No se pudo eliminar el servicio, respuesta no exitosa.")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("deleteService", "Error al intentar eliminar el servicio: ${t.message}")
            }
        })
    }


    private fun clearLastServiceId() {
        val sharedPreferences = requireActivity().getSharedPreferences("LastService", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("LastServiceId")
            apply()
        }
    }

    fun irAProcesoPublicacion() {
        val procesoPublicacionFragment = ProcesoPublicacion()
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, procesoPublicacionFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
