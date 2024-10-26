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

    private lateinit var imageView: ImageView
    lateinit var btnCerrarSesionFotoPerfil: Button
    lateinit var btnProfilesFotoPerfil: Button
    lateinit var btnCamara: Button
    lateinit var btnGaleria: Button
    private val profileViewModel: ProfileViewModel by viewModels()
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val storageRef = firebaseStorage.reference
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_foto_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.imageView)

        // Establecer la imagen predeterminada al iniciar
        btnCerrarSesionFotoPerfil = view.findViewById(R.id.btnCerrarSesionFotoPerfil)
        btnProfilesFotoPerfil = view.findViewById(R.id.btnProfilesFotoPerfil)
        btnCamara = view.findViewById(R.id.btnCamara)
        btnGaleria = view.findViewById(R.id.btnGaleria)
        imageView.setImageResource(R.drawable.hombremujer)

        // Observa los cambios en la URL de la imagen de perfil
        profileViewModel.profileImageUrl.observe(viewLifecycleOwner) { imageUrl ->
            Log.d("FotoPerfilFragment", "Image URL updated: $imageUrl")
            // Cargar la imagen en el ImageView
            if (imageUrl != null) {
                Picasso.get().load(imageUrl).into(imageView)
            } else {
                // Si no hay URL, puedes establecer la imagen por defecto
                imageView.setImageResource(R.drawable.hombremujer)
            }
        }

        btnCerrarSesionFotoPerfil.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
        }

        btnProfilesFotoPerfil.setOnClickListener {
            findNavController().navigate(R.id.action_fotoPerfilFragment_to_profilesFragment)
        }

        btnCamara.setOnClickListener {
            if (checkPermissions()) {
                openCamera()
            } else {
                requestPermissions()
            }
        }

        btnGaleria.setOnClickListener {
            if (checkPermissions()) {
                openGallery()
            } else {
                requestPermissions()
            }
        }
    }

    // ActivityResultLauncher for capturing a picture from the camera
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        bitmap?.let {
            val scaledBitmap = scaleBitmap(it, imageView.width, imageView.height)
            imageView.setImageBitmap(scaledBitmap)
            subirImagen(scaledBitmap)
        } ?: showToast("Failed to capture image")
    }

    // ActivityResultLauncher for picking an image from the gallery
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            try {
                // Carga el bitmap de la URI y redimensiona
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
                val scaledBitmap = scaleBitmap(bitmap, imageView.width, imageView.height)
                imageView.setImageBitmap(scaledBitmap)
                subirImagen(scaledBitmap)
            } catch (e: Exception) {
                showToast("Error loading image: ${e.message}")
            }
        } ?: showToast("Failed to select image")
    }

    // Function to scale the bitmap
    private fun scaleBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val ratio = Math.min(maxWidth.toFloat() / bitmap.width, maxHeight.toFloat() / bitmap.height)
        val width = (bitmap.width * ratio).toInt()
        val height = (bitmap.height * ratio).toInt()
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    fun subirImagen(bitmap: Bitmap) {
        // Obtengo el UID del usuario
        val userId = auth.currentUser?.uid ?: return

        // Genera un nombre de archivo único
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
                    profileViewModel.setProfileImageUrl(downloadURL)

                    val profiles = db.collection("Profiles")
                    profiles.document(userId).update("strAvatarUrl", downloadURL)
                        .addOnSuccessListener {
                            showToast("Profile image updated successfully!")
                            // Navegar o realizar cualquier otra acción necesaria
                        }
                        .addOnFailureListener {
                            showToast("Error updating profile image URL")
                        }
                }.addOnFailureListener {
                    showToast("Error retrieving download URL")
                }
            }
    }

    // Function to open the camera
    private fun openCamera() {
        takePictureLauncher.launch(null)
    }

    // Function to open the gallery
    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }

    // Check for necessary permissions
    private fun checkPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
        return cameraPermission == PackageManager.PERMISSION_GRANTED && storagePermission == PackageManager.PERMISSION_GRANTED
    }

    // ActivityResultLauncher for requesting permissions
    private val requestPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
        val storageGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false

        if (cameraGranted && storageGranted) {
            showToast("Permissions granted")
        } else {
            showToast("Permissions denied")
        }
    }

    // Request necessary permissions using ActivityResultLauncher
    private fun requestPermissions() {
        requestPermissionsLauncher.launch(
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        )
    }

    // Show toast message
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
