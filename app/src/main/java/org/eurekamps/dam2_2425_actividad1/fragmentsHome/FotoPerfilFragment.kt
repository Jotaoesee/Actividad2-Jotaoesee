package org.eurekamps.dam2_2425_actividad1.home_fragments

import android.Manifest
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.eurekamps.dam2_2425_actividad1.R
import org.eurekamps.dam2_2425_actividad1.viewmodel.ProfileViewModel
import java.io.ByteArrayOutputStream

class FotoPerfilFragment : Fragment() {

    private lateinit var imageView: ImageView
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Cambia aquí el layout al correspondiente a este fragmento
        return inflater.inflate(R.layout.fragment_foto_perfil, container, false)
    }

    // ActivityResultLauncher for capturing a picture from the camera
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        bitmap?.let {
            val scaledBitmap = scaleBitmap(it, imageView.width, imageView.height)
            imageView.setImageBitmap(scaledBitmap)
            subirImagen(scaledBitmap) // Pasa el bitmap escalado a subirImagen
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
                subirImagen(scaledBitmap) // Pasa el bitmap escalado a subirImagen
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view)

        imageView = view.findViewById(R.id.imageView)

        // Configura los botones para abrir la cámara y la galería
        val btnCamera = view.findViewById<Button>(R.id.btnCamara)
        val btnGallery = view.findViewById<Button>(R.id.btnGaleria)

        btnCamera.setOnClickListener {
            if (checkPermissions()) {
                openCamera()
            } else {
                requestPermissions()
            }
        }

        btnGallery.setOnClickListener {
            if (checkPermissions()) {
                openGallery()
            } else {
                requestPermissions()
            }
        }

        // Observa los cambios en la URL de la imagen de perfil
        profileViewModel.profileImageUrl.observe(viewLifecycleOwner) { imageUrl ->
            Log.d("FotoPerfilFragment", "Image URL updated: $imageUrl")
            // Aquí puedes manejar la URL de la imagen si es necesario
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
