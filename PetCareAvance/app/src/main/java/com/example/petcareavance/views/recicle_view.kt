import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petcareavance.Fragments.AnyServiceFragment
import com.example.petcareavance.R
import com.example.petcareavance.editPet
import com.example.petcareavance.room.AppDatabase
import com.example.petcareavance.room.serviceroom
import com.example.petcareavance.views.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.zip.Inflater

class RecycleViewFragment : Fragment() {

    interface ApiService {
        @GET("services")
        fun getUsers(): Call<List<UserResponse>>
    }

    data class UserResponse(
        val serviceId: Int,
        val price: Int,
        val description: String,
        val location: String,
        val phone: Int,
        val dni: Int,
        val cuidador: Boolean,
        val user: User,
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Cambia activity_main por servicerecicleview
        val view = inflater.inflate(R.layout.servicerecicleview, container, false)

        // Inicialización del Spinner
        val spinner: Spinner = view.findViewById(R.id.spinner)
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.opciones, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val fecha = arguments?.getString("FECHA")
        val direccion = arguments?.getString("DIRECCION")

        val fechaTextView: TextView = view.findViewById(R.id.fechaTextView) // Asegúrate de usar el ID correcto
        fechaTextView.text = fecha

        val direccionTextView: TextView = view.findViewById(R.id.editLocationText) // Asegúrate de usar el ID correcto
        direccionTextView.text = direccion

        val transaction = requireFragmentManager()

        getMyData(view,transaction)

        return view
    }

    private fun getMyData(view: View,transaction: FragmentManager) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiService::class.java)

        val retrofitData = retrofitBuilder.getUsers()

        retrofitData.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                val responseBody = response.body()!!

                val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = UserAdapter(responseBody,transaction)
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    class UserAdapter(private val userList: List<UserResponse>, private val transaction: FragmentManager) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

        class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val userName: TextView = itemView.findViewById(R.id.userName)
            val userIdAsPrice: TextView = itemView.findViewById(R.id.userIdAsPrice)
            val userAddress: TextView = itemView.findViewById(R.id.userAddress)
            val starIcon: ImageView = itemView.findViewById(R.id.starIcon)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_servicio, parent, false)
            return UserViewHolder(view)
        }

        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            val user = userList[position]
            holder.userName.text = user.user.firstName + " " +  user.user.lastName
            holder.userIdAsPrice.text = "S/. " + user.price.toString()
            holder.userAddress.text = user.location

            holder.userName.setOnClickListener{

                avanzarServicio(user.serviceId.toString())

            }

            holder.starIcon.setOnClickListener {
                it.isSelected = !it.isSelected
                if (it.isSelected) {

                    // Cambiar imagen a estrella encendida
                    holder.starIcon.setImageResource(android.R.drawable.btn_star_big_on)

                    // Guardar en la base de datos
                    CoroutineScope(Dispatchers.IO).launch {
                        val userResponse = serviceroom(
                            serviceId = user.serviceId,
                            price = user.price,
                            description = user.description,
                            location = user.location,
                            phone = user.phone,
                            dni = user.dni,
                            cuidador = user.cuidador,
                            user = user.user
                        )
                        AppDatabase.getDatabase(holder.itemView.context).userResponseDao().insert(userResponse)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(holder.itemView.context, "Servicio añadido a favoritos", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        override fun getItemCount() = userList.size

        private fun avanzarServicio(serviceId: String) {
            val anyServiceFragment = AnyServiceFragment()
            val bundle = Bundle()
            bundle.putString(anyServiceFragment.ARG_SERVICE_ID, serviceId)

            anyServiceFragment.arguments = bundle

            val transaction = transaction.beginTransaction()
            transaction.replace(R.id.fragment_container, anyServiceFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    companion object {
        const val BASE_URL = "https://petcarebackend.azurewebsites.net/api/v1/"
    }
}
