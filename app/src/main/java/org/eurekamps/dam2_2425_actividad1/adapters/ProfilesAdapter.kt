package org.eurekamps.dam2_2425_actividad1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.eurekamps.dam2_2425_actividad1.R
import org.eurekamps.dam2_2425_actividad1.fbClases.FbProfile

// Adaptador para manejar una lista de perfiles y vincularlos a un RecyclerView
class ProfileAdapter(private val profilesList: List<FbProfile>) :
    RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    // ViewHolder para almacenar las referencias a las vistas de cada elemento de la lista
    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Referencias a los TextView del layout del perfil
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvApellido: TextView = itemView.findViewById(R.id.tvApellido)
        val tvHobbies: TextView = itemView.findViewById(R.id.tvHobbie)
    }

    // Crea un nuevo ViewHolder inflando el layout XML para un perfil
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        // Infla el layout de un elemento individual de la lista de perfiles (profile_item.xml)
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.profile_item, parent, false)
        return ProfileViewHolder(itemView)
    }

    // Asigna los datos del perfil a las vistas del ViewHolder en una posición determinada
    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        // Obtiene el perfil correspondiente a la posición actual
        val profile = profilesList[position]
        // Asigna los valores del perfil a los TextViews correspondientes
        holder.tvNombre.text = profile.nombre
        holder.tvApellido.text = profile.apellidos
        holder.tvHobbies.text = profile.hobbies
    }

    // Devuelve el número total de perfiles en la lista
    override fun getItemCount() = profilesList.size
}
