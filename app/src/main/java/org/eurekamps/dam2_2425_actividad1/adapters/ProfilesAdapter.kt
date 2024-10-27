package org.eurekamps.dam2_2425_actividad1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import org.eurekamps.dam2_2425_actividad1.R
import org.eurekamps.dam2_2425_actividad1.fbClases.FbProfile

class ProfileAdapter(private var profilesList: MutableList<FbProfile>) :
    RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvApellido: TextView = itemView.findViewById(R.id.tvApellido)
        val tvHobbies: TextView = itemView.findViewById(R.id.tvHobbie)
        val imgPerfil: ImageView = itemView.findViewById(R.id.list_cell_ivAvatar)
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

        // Cargar la imagen con Picasso
        Picasso.get()
            .load(imageUrl)
            .placeholder(R.drawable.hombremujer) // Imagen de carga
            .error(R.drawable.hombremujer) // Imagen predeterminada en caso de error
            .into(holder.imgPerfil)

        // Establecer el listener de clic
        holder.itemView.setOnClickListener { view ->
            if (profile.uid == currentUserId) {
                view.findNavController().navigate(R.id.action_profilesFragment_to_seleccionProfileFragment)
            }
        }
    }

    override fun onViewRecycled(holder: ProfileViewHolder) {
        super.onViewRecycled(holder)
        // Libera recursos para evitar fugas de memoria
        holder.imgPerfil.setImageDrawable(null)
    }

    override fun getItemCount() = profilesList.size

    // Función para actualizar la lista de perfiles
    fun actualizarLista(nuevaLista: List<FbProfile>) {
        profilesList.clear()
        profilesList.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}
