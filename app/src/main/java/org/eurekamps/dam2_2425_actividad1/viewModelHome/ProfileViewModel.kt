package org.eurekamps.dam2_2425_actividad1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    // MutableLiveData para almacenar la URL de la imagen de perfil
    private val _profileImageUrl = MutableLiveData<String?>()

    // Exponer solo LiveData para evitar modificaciones externas
    val profileImageUrl: LiveData<String?> get() = _profileImageUrl

    // Método para establecer la URL de la imagen de perfil
    fun setProfileImageUrl(url: String?) {
        // Validar la URL antes de establecerla
        if (!url.isNullOrBlank()) {
            _profileImageUrl.value = url
        } else {
            // Manejar el caso de URL no válida
            _profileImageUrl.value = null // O puedes mantener el valor anterior
        }
    }
}
