package org.eurekamps.dam2_2425_actividad1.viewModelHome

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.eurekamps.dam2_2425_actividad1.fbClases.FbProfile

class PerfilViewModel : ViewModel() {

    // Instancia de Firebase Firestore y FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // LiveData para el perfil del usuario
    private val _perfil = MutableLiveData<FbProfile?>()
    val perfil: LiveData<FbProfile?> get() = _perfil

    // Función para guardar el perfil en Firestore
    fun guardarPerfil(perfilData: FbProfile, onComplete: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection("users").document(uid)
                .set(perfilData)
                .addOnSuccessListener {
                    onComplete(true)
                }
                .addOnFailureListener {
                    onComplete(false)
                }
        } else {
            onComplete(false)
        }
    }

    // Función para recuperar el perfil del usuario desde Firestore
    fun recuperarPerfil() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val perfil = document.toObject(FbProfile::class.java)
                        _perfil.value = perfil
                    } else {
                        _perfil.value = null
                    }
                }
                .addOnFailureListener {
                    _perfil.value = null
                }
        } else {
            _perfil.value = null
        }
    }

    fun descargarPerfil(uid: String) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val perfil = document.toObject(FbProfile::class.java)
                    // Usa el perfil recuperado como necesites
                } else {
                    Log.d("PerfilViewModel", "No se encontró el perfil")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("PerfilViewModel", "Error al recuperar perfil", exception)
            }
    }


    // Función para eliminar el perfil del usuario de Firestore
    fun eliminarPerfil(onComplete: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection("users").document(uid)
                .delete()
                .addOnSuccessListener {
                    _perfil.value = null
                    onComplete(true)
                }
                .addOnFailureListener {
                    onComplete(false)
                }
        } else {
            onComplete(false)
        }
    }
}
