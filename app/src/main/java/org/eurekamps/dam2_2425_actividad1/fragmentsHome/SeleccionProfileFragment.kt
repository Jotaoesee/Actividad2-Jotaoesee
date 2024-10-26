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

    lateinit var txNombreUsuario: TextView
    lateinit var txApellidosUsuario: TextView
    lateinit var txHobbiesUsuario: TextView
    lateinit var imgSeleccionada: ImageView
    lateinit var btnEditarFoto: Button

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

        txNombreUsuario = view.findViewById(R.id.nombreUsuario)
        txApellidosUsuario = view.findViewById(R.id.apellidosUsuario)
        txHobbiesUsuario = view.findViewById(R.id.hobbiesUsuario)
        imgSeleccionada = view.findViewById(R.id.imagenSeleccionada)
        btnEditarFoto = view.findViewById(R.id.editarFoto)

        // Establecer la imagen predeterminada al iniciar
        imgSeleccionada.setImageResource(R.drawable.hombremujer) // Cambia esto por tu imagen

        // Observar cambios en la URL de la imagen del perfil
        profileViewModel.profileImageUrl.observe(viewLifecycleOwner) { url ->
            if (url != null) {
                // Asignar la imagen de perfil
                Picasso.get().load(url).into(imgSeleccionada)
            } else {
                // Si la URL es nula, mantener la imagen predeterminada
                imgSeleccionada.setImageResource(R.drawable.hombremujer) // Cambia esto por tu imagen
            }
        }

        // Asignar los textos a los TextViews (debes manejar los demás campos en el ViewModel también si es necesario)
        val perfilNombre = "Nombre del perfil" // Obtén el nombre del perfil según sea necesario
        val perfilApellidos = "Apellidos del perfil" // Obtén los apellidos del perfil según sea necesario
        val perfilHobbies = "Hobbies del perfil" // Obtén los hobbies del perfil según sea necesario

        // Asignar valores a los TextViews
        txNombreUsuario.text = perfilNombre
        txApellidosUsuario.text = perfilApellidos
        txHobbiesUsuario.text = perfilHobbies

        // Asignar listener al botón "Editar Foto"
        btnEditarFoto.setOnClickListener {
            findNavController().navigate(R.id.action_seleccionProfileFragment_to_fotoPerfilFragment)
        }
    }
}
