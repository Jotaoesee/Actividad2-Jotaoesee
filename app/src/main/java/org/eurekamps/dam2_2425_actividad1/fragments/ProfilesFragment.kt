package org.eurekamps.dam2_2425_actividad1.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.eurekamps.dam2_2425_actividad1.R
import org.eurekamps.dam2_2425_actividad1.adapters.ProfileAdapter
import org.eurekamps.dam2_2425_actividad1.fbClases.FbProfile

class ProfilesFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerProfiles: RecyclerView
    private lateinit var profilesAdapter: ProfileAdapter
    private val profilesList = mutableListOf<FbProfile>()

    private lateinit var btnCerrarProfiles : Button
    private lateinit var btnPerfil: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profiles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Inicializar el RecyclerView
        recyclerProfiles = view.findViewById(R.id.listaPerfiles)
        btnPerfil = view.findViewById(R.id.btnPerfil)
        btnCerrarProfiles = view.findViewById(R.id.btnCerrarProfiles)
        recyclerProfiles.layoutManager = LinearLayoutManager(requireContext())
        profilesAdapter = ProfileAdapter(profilesList)
        recyclerProfiles.adapter = profilesAdapter

        // Llama a la función para recuperar y mostrar perfiles
        mostrarPerfiles()

        btnCerrarProfiles.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_profilesFragment_to_loginFragment)
        }

        btnPerfil.setOnClickListener{
            findNavController().navigate(R.id.action_profilesFragment_to_perfilFragment)
        }
    }

    private fun mostrarPerfiles() {
        // Recuperar todos los perfiles de la colección "users"
        db.collection("users").get()
            .addOnSuccessListener { result ->
                profilesList.clear() // Limpia la lista antes de llenarla
                for (document in result) {
                    val profile = document.toObject(FbProfile::class.java)
                    if (profile != null) { // Verifica que el perfil no sea nulo
                        profilesList.add(profile)
                    } else {
                        Log.w("ProfilesFragment", "Document ${document.id} is null")
                    }
                }
                profilesAdapter.notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
            }
            .addOnFailureListener { e ->
                Log.e("ProfilesFragment", "Error fetching profiles: ", e)
            }
    }
}
