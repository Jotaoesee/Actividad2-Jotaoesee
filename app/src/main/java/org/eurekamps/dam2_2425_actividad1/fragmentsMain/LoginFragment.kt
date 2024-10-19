package org.eurekamps.dam2_2425_actividad1.fragmentsMain

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import org.eurekamps.dam2_2425_actividad1.R
import org.eurekamps.dam2_2425_actividad1.statics.DataHolder


class LoginFragment : Fragment() {

    // Declaración de vistas
    lateinit var txEmail: EditText
    lateinit var txContraseña: EditText
    lateinit var txInicioSesion: TextView
    lateinit var btnLogin: Button
    lateinit var btnRegistrar: Button

    // FirebaseAuth para autenticación de usuarios
    private lateinit var auth: FirebaseAuth

    // Inicializa FirebaseAuth en la creación del fragmento
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtener instancia de FirebaseAuth
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    // Método llamado cuando la vista ha sido creada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicialización de vistas
        txEmail = view.findViewById(R.id.txEmailRegistro)
        txContraseña = view.findViewById(R.id.txContraseñaRegistro)
        txInicioSesion = view.findViewById(R.id.txInicio)
        btnLogin = view.findViewById(R.id.btnLogin)
        btnRegistrar = view.findViewById(R.id.btnRegistrarRegistro)

        // Listener para el botón de login
        btnLogin.setOnClickListener {
            Log.d("LoginFragment", "Login button clicked") // Log para depuración
            val email = txEmail.text.toString().trim() // Obtener y limpiar el texto del email
            val contrasenia = txContraseña.text.toString().trim() // Obtener y limpiar el texto de la contraseña

            // Validaciones de email y contraseña
            if (email.isEmpty()) {
                // Verifica si el campo email está vacío
                Toast.makeText(requireContext(), "Por favor, introduce un correo electrónico.", Toast.LENGTH_SHORT).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // Verifica si el email tiene un formato válido
                Toast.makeText(requireContext(), "Formato de correo electrónico inválido.", Toast.LENGTH_SHORT).show()
            } else if (contrasenia.isEmpty()) {
                // Verifica si el campo de la contraseña está vacío
                Toast.makeText(requireContext(), "Por favor, introduce una contraseña.", Toast.LENGTH_SHORT).show()
            } else {
                // Si todo es correcto, llama a la función para iniciar sesión
                iniciarSesion(email, contrasenia)
            }
        }

        // Listener para el botón de registro, navega al fragmento de registro
        btnRegistrar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registroFragment)
        }
    }

    // Función para iniciar sesión con email y contraseña en Firebase
    private fun iniciarSesion(email: String, contrasenia: String) {
        // Inicia sesión con Firebase Authentication
        auth.signInWithEmailAndPassword(email, contrasenia)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Si el inicio de sesión es exitoso, muestra un mensaje y navega al perfil
                    Toast.makeText(requireContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    DataHolder.descargarPerfil(requireActivity(), findNavController(), R.id.action_loginFragment_to_perfilFragment)
                } else {
                    // Maneja los errores en caso de fallo en la autenticación
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "El correo electrónico no está registrado."
                        is FirebaseAuthInvalidCredentialsException -> "La contraseña es incorrecta."
                        is FirebaseAuthException -> "Error: ${task.exception?.message}"
                        else -> "Error desconocido."
                    }
                    // Muestra el mensaje de error al usuario
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }
}
