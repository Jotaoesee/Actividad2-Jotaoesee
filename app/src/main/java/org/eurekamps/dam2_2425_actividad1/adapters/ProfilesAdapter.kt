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

    // ID del usuario actualmente autenticado en Firebase
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    // ViewHolder: contiene y administra los elementos de vista de cada perfil
    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvApellido: TextView = itemView.findViewById(R.id.tvApellido)
        val tvHobbies: TextView = itemView.findViewById(R.id.tvHobbie)
        val imgPerfil: ImageView = itemView.findViewById(R.id.list_cell_ivAvatar)
    }

    // Infla la vista de cada elemento de la lista desde el layout XML
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.profile_item, parent, false)
        return ProfileViewHolder(itemView)
    }

    // Configura cada elemento del ViewHolder con los datos del perfil correspondiente
    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profilesList[position]

        // Asigna valores de nombre, apellido y hobbies a los TextViews, mostrando texto por defecto si es nulo
        holder.tvNombre.text = profile.nombre ?: "Nombre no disponible"
        holder.tvApellido.text = profile.apellidos ?: "Apellido no disponible"
        holder.tvHobbies.text = profile.hobbies ?: "Hobbies no disponibles"

        // Cargar la URL de la imagen del perfil usando Picasso
        val imageUrl = profile.imagenUrl
        Picasso.get()
            .load(imageUrl)
            .placeholder(R.drawable.hombremujer) // Imagen predeterminada mientras carga
            .error(R.drawable.hombremujer) // Imagen predeterminada en caso de error
            .into(holder.imgPerfil)

        // Navega a la pantalla de selección de perfil si el perfil mostrado pertenece al usuario actual
        holder.itemView.setOnClickListener { view ->
            if (profile.uid == currentUserId) {
                view.findNavController().navigate(R.id.action_profilesFragment_to_seleccionProfileFragment)
            }
        }
    }

    // Libera recursos de la imagen para evitar fugas de memoria al reciclar el ViewHolder
    override fun onViewRecycled(holder: ProfileViewHolder) {
        super.onViewRecycled(holder)
        holder.imgPerfil.setImageDrawable(null) // Libera el ImageView
    }

    // Retorna el tamaño de la lista de perfiles
    override fun getItemCount() = profilesList.size

    // Actualiza la lista de perfiles y notifica al adaptador para refrescar la vista
    fun actualizarLista(nuevaLista: List<FbProfile>) {
        profilesList.clear()
        profilesList.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}
