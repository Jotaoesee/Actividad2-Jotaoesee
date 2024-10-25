package org.eurekamps.dam2_2425_actividad1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.eurekamps.dam2_2425_actividad1.R
import org.eurekamps.dam2_2425_actividad1.fbClases.FbProfile

class ProfileAdapter(private var profilesList: MutableList<FbProfile>) :
    RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvApellido: TextView = itemView.findViewById(R.id.tvApellido)
        val tvHobbies: TextView = itemView.findViewById(R.id.tvHobbie)
        val imgPerfil: ImageView = itemView.findViewById(R.id.imgPerfil)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.profile_item, parent, false)
        return ProfileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profilesList[position]

        holder.tvNombre.text = profile.nombre ?: "Nombre no disponible"
        holder.tvApellido.text = profile.apellidos ?: "Apellido no disponible"
        holder.tvHobbies.text = profile.hobbies ?: "Hobbies no disponibles"

        val imageUrl = profile.imagenUrl

        // Verifica si la URL de la imagen no es nula ni vacía antes de cargar con Picasso
        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.registro_bici) // Imagen mientras carga
                .error(R.drawable.registro_bici) // Imagen en caso de error
                .into(holder.imgPerfil)
        } else {
            // Establecer una imagen predeterminada si la URL es nula o vacía
            holder.imgPerfil.setImageResource(R.drawable.registro_bici)
        }
    }

    override fun getItemCount() = profilesList.size

    // Función para actualizar la lista de perfiles
    fun actualizarLista(nuevaLista: List<FbProfile>) {
        profilesList.clear()
        profilesList.addAll(nuevaLista)
        notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
    }
}
