package com.example.petcareavance.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petcareavance.R
import com.example.petcareavance.api.dataclasses.pets.PetResponse
import com.example.petcareavance.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PetFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pet, container, false)


        val rvListPet= view.findViewById<RecyclerView>(R.id.rvListPet)
        // Obtener User ID de SharedPreferences
        val sharedPreferences =
            requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val userId =  "12"//sharedPreferences.getString("ID", "") ?: ""

        // Obtener Token de SharedPreferences
        val sharedPreferences2 =
            requireActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("Token", "") ?: ""


        val call = RetrofitClient.instance.getPetByUser("Bearer " + token,userId)


        call.enqueue(object : Callback<List<PetResponse>> {
            override fun onResponse(
                call: Call<List<PetResponse>>,
                response: Response<List<PetResponse>>
            ) {
                val responseBody = response.body()!!

                rvListPet.layoutManager = LinearLayoutManager(requireContext())
                rvListPet.adapter = PetAdapter(responseBody)
            }

            override fun onFailure(call: Call<List<PetResponse>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        }

        )


       return view
    }

    class PetAdapter(private val petList: List<PetResponse>) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

        class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val petName: Button = itemView.findViewById(R.id.btNamePet)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pet, parent, false)
            return PetViewHolder(view)

        }

        override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
            val pet = petList[position]
            holder.petName.text = pet.name

        }

        override fun getItemCount() = petList.size
    }

}