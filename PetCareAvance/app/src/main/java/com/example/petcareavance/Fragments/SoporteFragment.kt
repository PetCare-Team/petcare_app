package com.example.petcareavance.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petcareavance.R
import com.example.petcareavance.api.RetrofitClient
import com.example.petcareavance.api.dataclasses.faqs.FaqItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SoporteFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var faqAdapter: FaqAdapter
    private val faqList = mutableListOf<FaqItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_soporte, container, false)
        val sharedPreferences = activity?.getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("ID", "No ID")

        recyclerView = view.findViewById(R.id.rvFAQs)
        recyclerView.layoutManager = LinearLayoutManager(context)
        faqAdapter = FaqAdapter(faqList)
        recyclerView.adapter = faqAdapter

        fetchFaqs()


        val btnIrAProceso = view.findViewById<TextView>(R.id.tvContact)
        btnIrAProceso.setOnClickListener {
            avanzar()
        }

        return view
    }

    private fun fetchFaqs() {
        val apiService = RetrofitClient.instance


        // Suponiendo que tienes un objeto apiService que provee el método getFaqs()
        val call = apiService.getFaqs() // Aquí asumo que necesitas un token, ajústalo según sea necesario
        call.enqueue(object : Callback<List<FaqItem>> {
            override fun onResponse(call: Call<List<FaqItem>>, response: Response<List<FaqItem>>) {
                if (response.isSuccessful) {
                    val faqsResponse = response.body()
                    if (faqsResponse != null) {
                        faqList.clear()
                        faqList.addAll(faqsResponse)
                        faqAdapter.notifyDataSetChanged()
                    }
                } else {
                    // Manejar el caso en que la respuesta no sea exitosa
                    Log.e("SoporteFragment", "Respuesta no exitosa: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Error al obtener FAQs", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<FaqItem>>, t: Throwable) {
                // Manejar el error de la llamada a la API
                Log.e("SoporteFragment", "Error en la llamada API: ${t.localizedMessage}")
                Toast.makeText(requireContext(), "Error de conexión: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })
    }


    fun avanzar() {
        val procesoPublicacionFragment = Contact()
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, procesoPublicacionFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}

class FaqAdapter(private val faqList: List<FaqItem>) : RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {

    class FaqViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionTextView: TextView = itemView.findViewById(R.id.tvQuestion)
        val answerTextView: TextView = itemView.findViewById(R.id.tvAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_faq, parent, false)
        return FaqViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val currentItem = faqList[position]
        holder.questionTextView.text = currentItem.question
        holder.answerTextView.text = currentItem.answer
    }

    override fun getItemCount() = faqList.size
}
