package org.eurekamps.dam2_2425_actividad1.viewModelMain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SplashViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Variable para indicar si el usuario está autenticado
    private val _usuarioAutenticado = MutableLiveData<Boolean>()
    val usuarioAutenticado: LiveData<Boolean> get() = _usuarioAutenticado

    // Función para verificar la autenticación del usuario
    fun verificarAutenticacionUsuario() {
        _usuarioAutenticado.value = auth.currentUser != null
    }
}
