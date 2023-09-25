import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
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

class RecycleViewFragment : Fragment() {

    interface ApiService {
        @GET("users")
        fun getUsers(): Call<List<UserResponse>>
    }

    data class UserResponse(
        val id: Int,
        val name: String,
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



        getMyData(view)

        return view
    }

    private fun getMyData(view: View) {
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
                recyclerView.adapter = UserAdapter(responseBody)
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    class UserAdapter(private val userList: List<UserResponse>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

        class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val userName: TextView = itemView.findViewById(R.id.userName)
            val userIdAsPrice: TextView = itemView.findViewById(R.id.userIdAsPrice)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_servicio, parent, false)
            return UserViewHolder(view)
        }

        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            val user = userList[position]
            holder.userName.text = user.name
            holder.userIdAsPrice.text = user.id.toString()
        }

        override fun getItemCount() = userList.size
    }

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }
}
