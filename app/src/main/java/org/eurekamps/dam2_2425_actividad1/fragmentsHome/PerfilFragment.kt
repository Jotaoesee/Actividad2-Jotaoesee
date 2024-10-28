package org.eurekamps.dam2_2425_actividad1.fragmentsHome

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.eurekamps.dam2_2425_actividad1.MainActivity
import org.eurekamps.dam2_2425_actividad1.R
import org.eurekamps.dam2_2425_actividad1.fbClases.FbProfile
import org.eurekamps.dam2_2425_actividad1.viewModelHome.PerfilViewModel

class PerfilFragment : Fragment() {

    // Instancia de Firestore para interactuar con la base de datos
    private val db = FirebaseFirestore.getInstance()

    // Instancia de FirebaseAuth para la autenticación del usuario
    private lateinit var auth: FirebaseAuth

    // Declaración de las vistas del layout
    lateinit var txNombre: EditText
    lateinit var txApellidos: EditText
    lateinit var txHobbies: EditText
    lateinit var txEdad: EditText
    lateinit var btnCerrarSesion: Button
    lateinit var btnGuardar: Button
    lateinit var btnMostrar: Button
    lateinit var btnEliminar: Button
    lateinit var btnIrProfiles: Button

    // ViewModel para el perfil del usuario
    private val perfilViewModel: PerfilViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicialización de FirebaseAuth
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento y retornarlo
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicialización de las vistas para interactuar con el usuario
        txNombre = view.findViewById(R.id.txNombrePerfil)
        txApellidos = view.findViewById(R.id.txApellidosPerfil)
        txHobbies = view.findViewById(R.id.txHobbies)
        txEdad = view.findViewById(R.id.txEdad)
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion)
        btnGuardar = view.findViewById(R.id.btnGuardar)
        btnMostrar = view.findViewById(R.id.btnMostrar)
        btnEliminar = view.findViewById(R.id.btnEliminar)
        btnIrProfiles = view.findViewById(R.id.btnIrProfiles)

        // Navega a ProfilesFragment al presionar btnIrProfiles
        btnIrProfiles.setOnClickListener {
            findNavController().navigate(R.id.action_perfilFragment_to_profilesFragment)
        }

        // Listener para cerrar sesión
        btnCerrarSesion.setOnClickListener {
            auth.signOut() // Cierra la sesión del usuario
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        // Listener para guardar perfil
        btnGuardar.setOnClickListener {
            val edadTexto = txEdad.text.toString()
            val edadInt = if (edadTexto.isNotBlank()) edadTexto.toIntOrNull() else null
            val perfilData = FbProfile(
                nombre = txNombre.text.toString(),
                apellidos = txApellidos.text.toString(),
                hobbies = txHobbies.text.toString(),
                edad = edadInt
            )


            perfilViewModel.guardarPerfil(perfilData) { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Perfil guardado con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error al guardar el perfil", Toast.LENGTH_SHORT).show()
                }
            }
        }




        // Listener para eliminar perfil
        btnEliminar.setOnClickListener {
            perfilViewModel.eliminarPerfil { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Perfil eliminado con éxito", Toast.LENGTH_SHORT).show()
                    // Limpia los campos del perfil
                    txNombre.text.clear()
                    txApellidos.text.clear()
                    txHobbies.text.clear()
                    txEdad.text.clear()
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar el perfil", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Listener para mostrar el perfil guardado al presionar el botón "Mostrar"
        btnMostrar.setOnClickListener {
            perfilViewModel.recuperarPerfil() // Llama al método para obtener el perfil
        }

        // Observador del perfil para actualizar los campos cuando haya cambios
        perfilViewModel.perfil.observe(viewLifecycleOwner) { perfil ->
            perfil?.let {
                txNombre.setText(it.nombre)
                txApellidos.setText(it.apellidos)
                txHobbies.setText(it.hobbies)
                txEdad.setText(it.edad.toString())
            } ?: Toast.makeText(requireContext(), "No se encontró perfil.", Toast.LENGTH_SHORT).show()
        }
    }
}
