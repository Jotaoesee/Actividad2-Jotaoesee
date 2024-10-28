package org.eurekamps.dam2_2425_actividad1.home_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.eurekamps.dam2_2425_actividad1.R
import org.eurekamps.dam2_2425_actividad1.viewmodel.ProfileViewModel

class SeleccionProfileFragment : Fragment() {

    lateinit var txNombreSeleccion: TextView
    lateinit var txApellidosSeleccion: TextView
    lateinit var txHobbiesSeleccion: TextView
    lateinit var txEdadSeleccion: TextView
    private lateinit var imgSeleccionada: ImageView
    private lateinit var btnEditarFoto: Button

    // Instancia del ViewModel para observar los datos del perfil
    private val profileViewModel: ProfileViewModel by viewModels()

    // Instancias de Firebase Authentication y Firestore para obtener el usuario y datos de perfil
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()// Inicializa Firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout del fragmento
        return inflater.inflate(R.layout.fragment_seleccion_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txNombreSeleccion = view.findViewById(R.id.txNombreSeleccion)
        txApellidosSeleccion = view.findViewById(R.id.txApellidosSeleccion)
        txHobbiesSeleccion = view.findViewById(R.id.txHobbiesSeleccion)
        txEdadSeleccion = view.findViewById(R.id.txEdadSeleccion)
        imgSeleccionada = view.findViewById(R.id.imagenSeleccionada)
        btnEditarFoto = view.findViewById(R.id.editarFoto)

        // Observador para la URL de la imagen de perfil en el ViewModel
        profileViewModel.urlImagenPerfil.observe(viewLifecycleOwner) { imagenUrl ->
            if (!imagenUrl.isNullOrEmpty()) {
                // Cargar la imagen de perfil si hay una URL válida
                cargarImagenPerfil(imagenUrl)
            } else {
                // Si no hay URL, mostrar una imagen predeterminada
                imgSeleccionada.setImageResource(R.drawable.hombremujer)
            }
        }

        // Listener para el botón "Editar Foto"
        btnEditarFoto.setOnClickListener {
            // Navega al fragmento para editar la foto de perfil
            findNavController().navigate(R.id.action_seleccionProfileFragment_to_fotoPerfilFragment)
        }

        // Llama a cargarImagenDesdeFirestore para cargar la imagen desde Firestore
        cargarDatosDePerfilDesdeFirestore()
    }

    private fun cargarDatosDePerfilDesdeFirestore() {
        val userId = auth.currentUser?.uid ?: return // Obtiene el ID del usuario actual
        val userProfileDocRef = db.collection("users").document(userId)

        // Accede a Firestore para obtener la URL de la imagen de perfil y los datos del perfil
        userProfileDocRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val imageUrl = document.getString("imagenUrl")
                    if (!imageUrl.isNullOrEmpty()) {
                        cargarImagenPerfil(imageUrl) // Carga la imagen si la URL es válida
                    } else {
                        imgSeleccionada.setImageResource(R.drawable.hombremujer) // Imagen predeterminada
                    }

                    // Cargar el nombre, apellidos y hobbies
                    txNombreSeleccion.text = document.getString("nombre") ?: "Nombre no disponible"
                    txApellidosSeleccion.text = document.getString("apellidos") ?: "Apellidos no disponibles"
                    txHobbiesSeleccion.text = document.getString("hobbies") ?: "Hobbies no disponibles"
                    txEdadSeleccion.text = document.getLong("edad")?.toString() ?: "Edad no disponible"

                } else {
                    // Si no existe el documento, establecer imágenes y textos predeterminados
                    imgSeleccionada.setImageResource(R.drawable.hombremujer) // Imagen predeterminada
                    txNombreSeleccion.text = "Nombre no disponible"
                    txApellidosSeleccion.text = "Apellidos no disponibles"
                    txHobbiesSeleccion.text = "Hobbies no disponibles"
                    txEdadSeleccion.text = "Edad no disponible"
                }
            }
            .addOnFailureListener { e ->
                Log.e("SeleccionProfileFragment", "Error al obtener los datos de perfil: ", e)
                // En caso de error, mostrar la imagen predeterminada y textos
                imgSeleccionada.setImageResource(R.drawable.hombremujer) // Imagen predeterminada
                txNombreSeleccion.text = "Nombre no disponible"
                txApellidosSeleccion.text = "Apellidos no disponibles"
                txHobbiesSeleccion.text = "Hobbies no disponibles"
                txEdadSeleccion.text = "Edad no disponible"
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
