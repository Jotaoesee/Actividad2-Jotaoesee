package org.eurekamps.dam2_2425_actividad1.home_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.eurekamps.dam2_2425_actividad1.R
import org.eurekamps.dam2_2425_actividad1.viewmodel.ProfileViewModel

class SeleccionProfileFragment : Fragment() {

    private lateinit var imgSeleccionada: ImageView
    private lateinit var btnEditarFoto: Button

    // Obtener la instancia del ViewModel
    private val profileViewModel: ProfileViewModel by viewModels()

    // Instancias de Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
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

        imgSeleccionada = view.findViewById(R.id.imagenSeleccionada)
        btnEditarFoto = view.findViewById(R.id.editarFoto)

        // Observar cambios en la URL de la imagen del perfil
        profileViewModel.urlImagenPerfil.observe(viewLifecycleOwner) { imagenUrl ->
            if (!imagenUrl.isNullOrEmpty()) {
                // Asignar la imagen de perfil si existe una URL válida
                cargarImagenPerfil(imagenUrl)
            } else {
                // Si no hay URL, establecer la imagen predeterminada
                imgSeleccionada.setImageResource(R.drawable.hombremujer)
            }
        }

        // Asignar listener al botón "Editar Foto"
        btnEditarFoto.setOnClickListener {
            findNavController().navigate(R.id.action_seleccionProfileFragment_to_fotoPerfilFragment)
        }

        // Llamar a cargarImagenDesdeFirestore al crear la vista
        cargarImagenDesdeFirestore()
    }

    private fun cargarImagenDesdeFirestore() {
        val userId = auth.currentUser?.uid ?: return
        val userProfileDocRef = db.collection("users").document(userId)

        // Obtener la URL de la imagen de perfil del Firestore
        userProfileDocRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val imageUrl = document.getString("imagenUrl")
                    if (!imageUrl.isNullOrEmpty()) {
                        // Si hay una imagen URL, cargarla
                        cargarImagenPerfil(imageUrl)
                    } else {
                        // Si no hay URL, mostrar la imagen predeterminada
                        imgSeleccionada.setImageResource(R.drawable.hombremujer)
                    }
                } else {
                    // Si no existe el documento del perfil, mostrar la imagen predeterminada
                    imgSeleccionada.setImageResource(R.drawable.hombremujer)
                }
            }
            .addOnFailureListener { e ->
                Log.e("SeleccionProfileFragment", "Error al obtener la imagen de perfil: ", e)
                // En caso de error, mostrar la imagen predeterminada
                imgSeleccionada.setImageResource(R.drawable.hombremujer)
            }
    }

    private fun cargarImagenPerfil(imagenUrl: String) {
        Log.d("SeleccionProfileFragment", "Cargando imagen desde: $imagenUrl")
        Picasso.get().load(imagenUrl).into(imgSeleccionada, object : Callback {
            override fun onSuccess() {
                Log.d("SeleccionProfileFragment", "Imagen cargada exitosamente.")
            }

            override fun onError(e: Exception?) {
                Log.e("SeleccionProfileFragment", "Error al cargar la imagen: ${e?.message}")
                imgSeleccionada.setImageResource(R.drawable.hombremujer)
            }
        })
    }
}
