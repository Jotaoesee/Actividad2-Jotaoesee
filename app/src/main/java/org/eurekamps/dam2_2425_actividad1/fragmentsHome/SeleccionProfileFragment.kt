package org.eurekamps.dam2_2425_actividad1.home_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import org.eurekamps.dam2_2425_actividad1.R
import org.eurekamps.dam2_2425_actividad1.viewmodel.ProfileViewModel

class SeleccionProfileFragment : Fragment() {

    lateinit var nombreUsuario: TextView
    lateinit var apellidosUsuario: TextView
    lateinit var hobbiesUsuario: TextView
    lateinit var imagenSeleccionada: ImageView
    lateinit var editarFoto: Button

    // Obtener la instancia del ViewModel
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seleccion_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nombreUsuario = view.findViewById(R.id.nombreUsuario)
        apellidosUsuario = view.findViewById(R.id.apellidosUsuario)
        hobbiesUsuario = view.findViewById(R.id.hobbiesUsuario)
        imagenSeleccionada = view.findViewById(R.id.imagenSeleccionada)
        editarFoto = view.findViewById(R.id.editarFoto)

        // Observar cambios en la URL de la imagen del perfil
        profileViewModel.profileImageUrl.observe(viewLifecycleOwner) { url ->
            if (url != null) {
                // Asignar la imagen de perfil
                val imgProfileSelected: ImageView = view.findViewById(R.id.imagenSeleccionada)
                Picasso.get().load(url).into(imgProfileSelected)
            }
        }

        // Asignar los textos a los TextViews (debes manejar los demás campos en el ViewModel también si es necesario)
        val perfilNombre = "Nombre del perfil" // Obtén el nombre del perfil según sea necesario
        val perfilApellidos = "Apellidos del perfil" // Obtén los apellidos del perfil según sea necesario
        val perfilHobbies = "Hobbies del perfil" // Obtén los hobbies del perfil según sea necesario

        // Asignar listener al botón "Edit"
        view.findViewById<Button>(R.id.editarFoto).setOnClickListener {
            findNavController().navigate(R.id.action_seleccionProfileFragment_to_fotoPerfilFragment)
        }
    }
}
