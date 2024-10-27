package org.eurekamps.dam2_2425_actividad1.home_fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import org.eurekamps.dam2_2425_actividad1.MainActivity
import org.eurekamps.dam2_2425_actividad1.R
import org.eurekamps.dam2_2425_actividad1.viewmodel.ProfileViewModel
import java.io.ByteArrayOutputStream

class FotoPerfilFragment : Fragment() {

    // Declaración de variables UI
    private lateinit var imageView: ImageView
    lateinit var btnCerrarSesionFotoPerfil: Button
    lateinit var btnProfilesFotoPerfil: Button
    lateinit var btnCamara: Button
    lateinit var btnGaleria: Button

    // ViewModel para gestionar el perfil del usuario
    private val profileViewModel: ProfileViewModel by viewModels()

    // Referencias a Firebase Storage y Firestore
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val storageRef = firebaseStorage.reference
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    // Inicializa FirebaseAuth y FirebaseFirestore en el `onCreate`
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    // Infla el layout del fragmento
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_foto_perfil, container, false)
    }

    // Configura la interfaz y los listeners después de que la vista está creada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Asignación de vistas a variables
        imageView = view.findViewById(R.id.imageView)
        btnCerrarSesionFotoPerfil = view.findViewById(R.id.btnCerrarSesionFotoPerfil)
        btnProfilesFotoPerfil = view.findViewById(R.id.btnProfilesFotoPerfil)
        btnCamara = view.findViewById(R.id.btnCamara)
        btnGaleria = view.findViewById(R.id.btnGaleria)

        // Configuración de imagen predeterminada
        imageView.setImageResource(R.drawable.hombremujer)

        // Carga la imagen de perfil desde Firestore si está disponible
        cargarImagenPerfil()

        // Botón para cerrar sesión
        btnCerrarSesionFotoPerfil.setOnClickListener {
            cerrarSesion()
        }

        // Botón para ir a la lista de perfiles
        btnProfilesFotoPerfil.setOnClickListener {
            findNavController().navigate(R.id.action_fotoPerfilFragment_to_profilesFragment)
        }

        // Botón para abrir la cámara si los permisos están concedidos
        btnCamara.setOnClickListener {
            if (checkPermissions()) {
                openCamera()
            } else {
                requestPermissions()
            }
        }

        // Botón para abrir la galería si los permisos están concedidos
        btnGaleria.setOnClickListener {
            if (checkPermissions()) {
                openGallery()
            } else {
                requestPermissions()
            }
        }
    }

    // Cargar imagen de perfil desde Firebase Firestore
    private fun cargarImagenPerfil() {
        val userId = auth.currentUser?.uid ?: return
        val userProfileDocRef = db.collection("users").document(userId)

        userProfileDocRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val imageUrl = document.getString("imagenUrl")
                    if (!imageUrl.isNullOrEmpty()) {
                        Picasso.get().load(imageUrl).into(imageView)
                    } else {
                        imageView.setImageResource(R.drawable.hombremujer)
                    }
                } else {
                    imageView.setImageResource(R.drawable.hombremujer)
                }
            }
            .addOnFailureListener { e ->
                Log.e("FotoPerfilFragment", "Error fetching profile image: ", e)
                imageView.setImageResource(R.drawable.hombremujer)
            }
    }

    // Cerrar la sesión del usuario y volver a la actividad principal
    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    // Lanzador para tomar una foto con la cámara y procesar el resultado
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        bitmap?.let {
            val scaledBitmap = scaleBitmap(it, imageView.width, imageView.height)
            imageView.setImageBitmap(scaledBitmap)
            subirImagen(scaledBitmap)
        } ?: showToast("Failed to capture image")
    }

    // Lanzador para seleccionar una imagen de la galería y procesar el resultado
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
                val scaledBitmap = scaleBitmap(bitmap, imageView.width, imageView.height)
                imageView.setImageBitmap(scaledBitmap)
                subirImagen(scaledBitmap)
            } catch (e: Exception) {
                showToast("Error loading image: ${e.message}")
            }
        } ?: showToast("Failed to select image")
    }

    // Escala la imagen capturada o seleccionada al tamaño del ImageView
    private fun scaleBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val ratio = Math.min(maxWidth.toFloat() / bitmap.width, maxHeight.toFloat() / bitmap.height)
        val width = (bitmap.width * ratio).toInt()
        val height = (bitmap.height * ratio).toInt()
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    // Sube la imagen a Firebase Storage y actualiza Firestore con el enlace de descarga
    fun subirImagen(bitmap: Bitmap) {
        val userId = auth.currentUser?.uid ?: return

        val fileName = "profile_pictures/${userId}_profile.jpg"
        val profileImageRef = storageRef.child(fileName)

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        profileImageRef.putBytes(data)
            .addOnFailureListener {
                showToast("Error uploading image")
            }
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    val downloadURL = uri.toString()
                    profileViewModel.establecerUrlImagenPerfil(downloadURL)

                    val profiles = db.collection("users")
                    val userProfileDocRef = profiles.document(userId)

                    userProfileDocRef.get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                userProfileDocRef.update("imagenUrl", downloadURL)
                                    .addOnSuccessListener {
                                        showToast("Profile image updated successfully!")
                                    }
                                    .addOnFailureListener {
                                        showToast("Error updating profile image URL")
                                    }
                            } else {
                                val newProfileData = mapOf("imagenUrl" to downloadURL)
                                userProfileDocRef.set(newProfileData)
                                    .addOnSuccessListener {
                                        showToast("Profile created and image URL saved!")
                                    }
                                    .addOnFailureListener {
                                        showToast("Error creating profile document")
                                    }
                            }
                        }
                        .addOnFailureListener {
                            showToast("Error checking profile document existence")
                        }
                }.addOnFailureListener {
                    showToast("Error retrieving download URL")
                }
            }
    }

    // Abre la cámara para capturar una foto
    private fun openCamera() {
        takePictureLauncher.launch(null)
    }

    // Abre la galería para seleccionar una imagen
    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }

    // Verifica si los permisos necesarios están concedidos
    private fun checkPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
        return cameraPermission == PackageManager.PERMISSION_GRANTED && storagePermission == PackageManager.PERMISSION_GRANTED
    }

    // Solicita los permisos necesarios
    private val requestPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
        val storageGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false

        if (cameraGranted && storageGranted) {
            showToast("Permissions granted")
        } else {
            showToast("Permissions denied")
        }
    }

    // Lanza la solicitud de permisos necesarios
    private fun requestPermissions() {
        requestPermissionsLauncher.launch(
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        )
    }

    // Muestra un mensaje en pantalla
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
