package org.eurekamps.dam2_2425_actividad1.fragmentsHome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.eurekamps.dam2_2425_actividad1.MainActivity
import org.eurekamps.dam2_2425_actividad1.R
import org.eurekamps.dam2_2425_actividad1.adapters.ProfileAdapter
import org.eurekamps.dam2_2425_actividad1.fbClases.FbProfile

class ProfilesFragment : Fragment() {

    // Instancia de Firebase Firestore para acceder a la base de datos
    private val db = FirebaseFirestore.getInstance()
    // Instancia de FirebaseAuth para manejar la autenticación del usuario
    private lateinit var auth: FirebaseAuth
    // RecyclerView para mostrar la lista de perfiles
    private lateinit var recyclerProfiles: RecyclerView
    // Adaptador para manejar la lista de perfiles
    private lateinit var profilesAdapter: ProfileAdapter
    // Lista mutable que almacena los perfiles recuperados
    private val profilesList = mutableListOf<FbProfile>()
    // Botón para cerrar sesión
    private lateinit var btnCerrarProfiles: Button
    // Botón para ir al perfil
    private lateinit var btnIrAPerfil: Button
    // Botón para ver perfiles mayores de 35 años
    private lateinit var btnPerfilesMayores35: Button
    // Botón para ver todos los perfiles
    private lateinit var btnTodosPerfiles: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento (fragment_profiles.xml)
        return inflater.inflate(R.layout.fragment_profiles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa la instancia de autenticación de Firebase
        auth = FirebaseAuth.getInstance()

        // Inicializar el RecyclerView y configurar su layout manager
        recyclerProfiles = view.findViewById(R.id.listaPerfiles)
        btnCerrarProfiles = view.findViewById(R.id.btnCerrarProfiles)
        btnIrAPerfil = view.findViewById(R.id.btnIrPerfil)
        btnPerfilesMayores35 = view.findViewById(R.id.btnPerfilesMayores35)
        btnTodosPerfiles = view.findViewById(R.id.btnTodosPerfiles)
        recyclerProfiles.layoutManager = LinearLayoutManager(requireContext())

        // Inicializar el adaptador del RecyclerView con la lista vacía de perfiles
        profilesAdapter = ProfileAdapter(profilesList)
        recyclerProfiles.adapter = profilesAdapter

        // Llama a la función para recuperar y mostrar todos los perfiles de la base de datos
        mostrarPerfiles()

        // Configurar el botón de cerrar sesión
        btnCerrarProfiles.setOnClickListener {
            // Cierra la sesión del usuario actual
            auth.signOut()
            // Redirige a la MainActivity después de cerrar la sesión
            val intent = Intent(requireActivity(), MainActivity::class.java)
            requireActivity().startActivity(intent)
            requireActivity().finish()
        }

        btnIrAPerfil.setOnClickListener {
            findNavController().navigate(R.id.action_profilesFragment_to_perfilFragment)
        }

        btnPerfilesMayores35.setOnClickListener {
            mostrarPerfilesMayoresDe35()
        }

        btnTodosPerfiles.setOnClickListener {
            mostrarPerfiles()
        }
    }

    // Función para recuperar todos los perfiles desde Firestore y actualizarlos en el RecyclerView
    private fun mostrarPerfiles() {
        db.collection("users").get()
            .addOnSuccessListener { result ->
                profilesList.clear() // Limpia la lista antes de agregar nuevos perfiles
                for (document in result) {
                    val profile = FbProfile(
                        uid = document.id,
                        nombre = document.getString("nombre") ?: "",
                        edad = document.getLong("edad")?.toInt() ?: 0,
                        apellidos = document.getString("apellidos") ?: "",
                        hobbies = document.getString("hobbies") ?: "",
                        imagenUrl = document.getString("imagenUrl")
                    )
                    profilesList.add(profile)
                }
                profilesAdapter.notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
                if (profilesList.isEmpty()) {
                    Toast.makeText(requireContext(), "No hay perfiles disponibles.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("ProfilesFragment", "Error fetching profiles: ", e)
                Toast.makeText(requireContext(), "Error al recuperar perfiles.", Toast.LENGTH_SHORT).show()
            }
    }

    // Función para recuperar perfiles mayores de 35 años desde Firestore
    private fun mostrarPerfilesMayoresDe35() {
        db.collection("users")
            .whereGreaterThan("edad", 35) // Consulta para filtrar por edad
            .get()
            .addOnSuccessListener { result ->
                profilesList.clear() // Limpia la lista antes de agregar nuevos perfiles
                for (document in result) {
                    val profile = FbProfile(
                        uid = document.id,
                        nombre = document.getString("nombre") ?: "",
                        edad = document.getLong("edad")?.toInt() ?: 0,
                        apellidos = document.getString("apellidos") ?: "",
                        hobbies = document.getString("hobbies") ?: "",
                        imagenUrl = document.getString("imagenUrl")
                    )
                    profilesList.add(profile)
                }
                profilesAdapter.notifyDataSetChanged()
                if (profilesList.isEmpty()) {
                    Toast.makeText(requireContext(), "No hay perfiles mayores de 35 años disponibles.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("ProfilesFragment", "Error fetching older profiles: ", e)
                Toast.makeText(requireContext(), "Error al recuperar perfiles mayores de 35 años.", Toast.LENGTH_SHORT).show()
            }
    }
}
