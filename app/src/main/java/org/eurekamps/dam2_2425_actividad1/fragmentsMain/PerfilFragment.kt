package org.eurekamps.dam2_2425_actividad1.fragmentsMain

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.eurekamps.dam2_2425_actividad1.HomeActivity
import org.eurekamps.dam2_2425_actividad1.R
import org.eurekamps.dam2_2425_actividad1.fbClases.FbProfile

class PerfilFragment : Fragment() {

    // Instancia de Firestore para interactuar con la base de datos
    private val db = FirebaseFirestore.getInstance()

    // Instancia de FirebaseAuth para gestionar la autenticación
    private lateinit var auth: FirebaseAuth

    // Declaración de las vistas
    lateinit var txNombre: EditText
    lateinit var txApellidos: EditText
    lateinit var txHobbies: EditText
    lateinit var btnCerrarSesion: Button
    lateinit var btnGuardar: Button
    lateinit var btnMostrar: Button
    lateinit var btnEliminar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicialización de las vistas
        txNombre = view.findViewById(R.id.txNombrePerfil)
        txApellidos = view.findViewById(R.id.txApellidosPerfil)
        txHobbies = view.findViewById(R.id.txHobbies)
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion)
        btnGuardar = view.findViewById(R.id.btnGuardar)
        btnMostrar = view.findViewById(R.id.btnMostrar)
        btnEliminar = view.findViewById(R.id.btnEliminar)

        // Listener para cerrar sesión
        btnCerrarSesion.setOnClickListener {
            auth.signOut() // Cierra la sesión de Firebase
            findNavController().navigate(R.id.action_perfilFragment_to_loginFragment) // Navega al login
        }

        // Listener para guardar perfil
        btnGuardar.setOnClickListener {
            guardarPerfil() // Llama a la función que guarda el perfil
        }

        // Listener para eliminar perfil
        btnEliminar.setOnClickListener {
            eliminarPerfil() // Llama a la función que elimina el perfil
        }

        // Listener para mostrar el perfil guardado
        btnMostrar.setOnClickListener {
            recuperarPerfil() // Llama a la función que recupera los datos del perfil
        }
    }

    // Función para guardar el perfil en Firebase Firestore
    private fun guardarPerfil() {
        val uid = auth.currentUser?.uid // Obtiene el UID del usuario autenticado

        if (uid != null) {
            // Obtiene los datos de los campos de texto
            val nombre = txNombre.text.toString()
            val apellidos = txApellidos.text.toString()
            val hobbies = txHobbies.text.toString()
            val avatar = "url_o_ruta_del_avatar"



            // Crea un objeto de la clase FbProfile con los datos ingresados
            val perfilData = FbProfile(
                nombre = nombre,
                apellidos = apellidos,
                hobbies = hobbies,
                avatar = avatar
            )

            // Guarda los datos en la colección "users" usando el UID del usuario como el ID del documento
            db.collection("users").document(uid) // Documento con el UID como ID
                .set(perfilData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Perfil guardado exitosamente.", Toast.LENGTH_SHORT).show()
                    // Navega a la actividad principal después de guardar el perfil
                    val intent = Intent(requireActivity(), HomeActivity::class.java)
                    requireActivity().startActivity(intent)
                    requireActivity().finish()
                }
                .addOnFailureListener { e ->
                    // Muestra un error si la operación falla
                    Toast.makeText(requireContext(), "Error al guardar perfil: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("PerfilFragment", "Error al guardar perfil", e)
                }
        } else {
            Toast.makeText(requireContext(), "Error: No se ha encontrado el usuario autenticado.", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para recuperar el perfil del usuario desde Firestore
    private fun recuperarPerfil() {
        val uid = auth.currentUser?.uid // Obtiene el UID del usuario autenticado

        if (uid != null) {
            // Recupera los datos del perfil del documento del usuario
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        // Si el documento existe, muestra los datos en los campos correspondientes
                        txNombre.setText(document.getString("nombre"))
                        txApellidos.setText(document.getString("apellidos"))
                        txHobbies.setText(document.getLong("edad")?.toString())
                        Toast.makeText(requireContext(), "Perfil recuperado exitosamente.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "No se encontró el perfil.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error al recuperar perfil: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("PerfilFragment", "Error al recuperar perfil", e)
                }
        } else {
            Toast.makeText(requireContext(), "Error: No se ha encontrado el usuario autenticado.", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para eliminar el perfil del usuario de Firestore
    private fun eliminarPerfil() {
        val uid = auth.currentUser?.uid // Obtiene el UID del usuario autenticado

        if (uid != null) {
            // Elimina el documento del usuario en la colección "users"
            db.collection("users").document(uid)
                .delete()
                .addOnSuccessListener {
                    // Limpia los campos de texto después de eliminar el perfil
                    txNombre.setText("")
                    txApellidos.setText("")
                    txHobbies.setText("")

                    Toast.makeText(requireContext(), "Perfil eliminado exitosamente.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error al eliminar perfil: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("PerfilFragment", "Error al eliminar perfil", e)
                }
        } else {
            Toast.makeText(requireContext(), "Error: No se ha encontrado el usuario autenticado.", Toast.LENGTH_SHORT).show()
        }
    }
}
