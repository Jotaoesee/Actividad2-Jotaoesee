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
        super.onViewCreated(view)

        // Observar cambios en la URL de la imagen del perfil
        profileViewModel.profileImageUrl.observe(viewLifecycleOwner) { url ->
            if (url != null) {
                // Asignar la imagen de perfil
                val imgProfileSelected: ImageView = view.findViewById(R.id.ivProfileSelected)
                Picasso.get().load(url).into(imgProfileSelected)
            }
        }

        // Asignar los textos a los TextViews (debes manejar los demás campos en el ViewModel también si es necesario)
        val profileName = "Nombre del perfil" // Obtén el nombre del perfil según sea necesario
        view.findViewById<TextView>(R.id.list_cell_txtview_name).text = "Nombre: $profileName"

        // Asignar listener al botón "Edit"
        view.findViewById<Button>(R.id.button8).setOnClickListener {
            findNavController().navigate(R.id.action_homeSelectProfFragment_to_PhotoProfileFragment)
        }
    }
}
