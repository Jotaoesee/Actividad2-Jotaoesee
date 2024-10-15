package org.eurekamps.dam2_2425_actividad1.fragments

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
import org.eurekamps.dam2_2425_actividad1.R
import org.eurekamps.dam2_2425_actividad1.fbClases.FbProfile


class PerfilFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    lateinit var txNombre: EditText
    lateinit var txApellidos: EditText
    lateinit var txEdad: EditText
    lateinit var txNumero: EditText
    lateinit var btnCerrarSesion: Button
    lateinit var btnGuardar: Button
    lateinit var btnMostrar: Button
    lateinit var btnEliminar: Button
    lateinit var btnIrProfiles: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txNombre = view.findViewById(R.id.txNombrePerfil)
        txApellidos = view.findViewById(R.id.txApellidosPerfil)
        txEdad = view.findViewById(R.id.txEdadPerfil)
        txNumero = view.findViewById(R.id.txNumeroPerfil)
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion)
        btnGuardar = view.findViewById(R.id.btnGuardar)
        btnMostrar = view.findViewById(R.id.btnMostrar)
        btnEliminar = view.findViewById(R.id.btnEliminar)
        btnIrProfiles = view.findViewById(R.id.btnIrProfiles)

        btnCerrarSesion.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_perfilFragment_to_loginFragment)
        }

        btnGuardar.setOnClickListener {
            guardarPerfil()
        }

        btnEliminar.setOnClickListener {
            eliminarPerfil()
        }

        btnMostrar.setOnClickListener {
            recuperarPerfil()
        }

        btnIrProfiles.setOnClickListener{
            findNavController().navigate(R.id.action_perfilFragment_to_profilesFragment)
        }
    }

    private fun guardarPerfil() {
        val uid = auth.currentUser?.uid // Obtén el UID del usuario autenticado

        if (uid != null) {
            val nombre = txNombre.text.toString()
            val apellidos = txApellidos.text.toString()
            val edad = txEdad.text.toString().toIntOrNull()
            val telefono = txNumero.text.toString()

            // Crea un objeto de la Data Class FbProfile
            val perfilData = FbProfile(
                Nombre = nombre,
                Apellidos = apellidos,
                Edad = edad,
                Telefono = telefono
            )

            // Guardar los datos en Firestore usando el UID como ID del documento
            db.collection("users").document(uid) // Usa el UID como ID del documento
                .set(perfilData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Perfil guardado exitosamente.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error al guardar perfil: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("PerfilFragment", "Error al guardar perfil", e)
                }
        } else {
            Toast.makeText(requireContext(), "Error: No se ha encontrado el usuario autenticado.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun recuperarPerfil() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        txNombre.setText(document.getString("nombre"))
                        txApellidos.setText(document.getString("apellidos"))
                        txEdad.setText(document.getLong("edad")?.toString())
                        txNumero.setText(document.getString("telefono"))

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

    private fun eliminarPerfil() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection("users").document(uid)
                .delete()
                .addOnSuccessListener {
                    // Limpiar los campos de texto
                    txNombre.setText("")
                    txApellidos.setText("")
                    txEdad.setText("")
                    txNumero.setText("")

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
