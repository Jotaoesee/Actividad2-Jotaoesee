package org.eurekamps.dam2_2425_actividad1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.eurekamps.dam2_2425_actividad1.R
import org.eurekamps.dam2_2425_actividad1.fbClases.FbProfile

class ProfileAdapter(private val profilesList: List<FbProfile>) :
    RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvApellido: TextView = itemView.findViewById(R.id.tvApellido)
        val tvEdad: TextView = itemView.findViewById(R.id.tvEdad)
        val tvTelefonoMovil: TextView = itemView.findViewById(R.id.tvTelefonoMovil)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.profile_item, parent, false)
        return ProfileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profilesList[position]
        holder.tvNombre.text = profile.nombre
        holder.tvApellido.text = profile.apellidos
        holder.tvEdad.text = "Edad: ${profile.edad}"
        holder.tvTelefonoMovil.text = "Teléfono: ${profile.telefono}"
    }

    override fun getItemCount() = profilesList.size
}
