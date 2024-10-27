package org.eurekamps.dam2_2425_actividad1.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    // MutableLiveData para almacenar la URL de la imagen de perfil
    private val _urlImagenPerfil = MutableLiveData<String?>()

    // Exponer solo LiveData para evitar modificaciones externas
    val urlImagenPerfil: LiveData<String?> get() = _urlImagenPerfil

    // Método para establecer la URL de la imagen de perfil
    fun establecerUrlImagenPerfil(url: String?) {
        // Validar la URL antes de establecerla
        if (!url.isNullOrBlank()) {
            _urlImagenPerfil.value = url
            Log.d("ProfileViewModel", "URL de la imagen de perfil establecida: $url")
        } else {
            Log.d("ProfileViewModel", "URL no válida recibida, manteniendo el valor anterior: ${_urlImagenPerfil.value}")
        }
    }

}
