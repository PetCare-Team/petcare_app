package com.example.petcareavance.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petcareavance.R
import com.example.petcareavance.room.AppDatabase
import com.example.petcareavance.room.serviceroom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesFragment : Fragment() {

    private var serviceList = mutableListOf<serviceroom>() // Cambiado a mutable para poder actualizarlo
    private lateinit var adapter: FavoritesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewFavorites)
        val returnbtn: ImageView = view.findViewById(R.id.imageView32)

        returnbtn.setOnClickListener{
            parentFragmentManager.popBackStack()

        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Inicializar el adaptador
        adapter = FavoritesAdapter(serviceList)
        recyclerView.adapter = adapter

        loadData()

        return view
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            serviceList.clear()  // Limpiar la lista
            serviceList.addAll(AppDatabase.getDatabase(requireContext()).userResponseDao().getAll())
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged() // Notificar al adaptador del cambio
            }
        }
    }

    inner class FavoritesAdapter(private val serviceList: List<serviceroom>) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

        inner class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val userName: TextView = itemView.findViewById(R.id.userName)
            val userAddress: TextView = itemView.findViewById(R.id.userAddress)
            val userIdAsPrice: TextView = itemView.findViewById(R.id.userIdAsPrice)
            val starIcon: ImageView = itemView.findViewById(R.id.starIcon)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorito, parent, false)
            return FavoritesViewHolder(view)
        }

        override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
            val service = serviceList[position]
            holder.userName.text = service.user.firstName + " " +  service.user.lastName
            holder.userIdAsPrice.text = "S/. " + service.price.toString()
            holder.userAddress.text = service.location

            holder.starIcon.setOnClickListener {
                it.isSelected = !it.isSelected
                if (!it.isSelected) {
                    // Eliminar de la base de datos
                    CoroutineScope(Dispatchers.IO).launch {
                        AppDatabase.getDatabase(holder.itemView.context).userResponseDao().delete(service)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(holder.itemView.context, "Servicio eliminado de favoritos", Toast.LENGTH_SHORT).show()
                            loadData()  // Volver a cargar los datos después de eliminar un elemento
                        }
                    }
                }
            }
        }
        override fun getItemCount() = serviceList.size
    }
}
