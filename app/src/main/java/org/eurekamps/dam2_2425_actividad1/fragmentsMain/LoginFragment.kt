package org.eurekamps.dam2_2425_actividad1.fragmentsMain

import android.content.Intent
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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import org.eurekamps.dam2_2425_actividad1.HomeActivity
import org.eurekamps.dam2_2425_actividad1.R
import org.eurekamps.dam2_2425_actividad1.viewModelHome.PerfilViewModel

class LoginFragment : Fragment() {

    // Declaración de vistas
    private lateinit var txEmail: EditText
    private lateinit var txContraseña: EditText
    private lateinit var txInicioSesion: TextView
    private lateinit var btnLogin: Button
    private lateinit var btnRegistrar: Button

    // FirebaseAuth para autenticación de usuarios
    private lateinit var auth: FirebaseAuth
    // ViewModel para manejar el perfil
    private val perfilViewModel: PerfilViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txEmail = view.findViewById(R.id.txEmailRegistro)
        txContraseña = view.findViewById(R.id.txContraseñaRegistro)
        txInicioSesion = view.findViewById(R.id.txInicio)
        btnLogin = view.findViewById(R.id.btnLogin)
        btnRegistrar = view.findViewById(R.id.btnRegistrarRegistro)

        btnLogin.setOnClickListener {
            val email = txEmail.text.toString().trim()
            val contrasenia = txContraseña.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, introduce un correo electrónico.", Toast.LENGTH_SHORT).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(requireContext(), "Formato de correo electrónico inválido.", Toast.LENGTH_SHORT).show()
            } else if (contrasenia.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, introduce una contraseña.", Toast.LENGTH_SHORT).show()
            } else {
                iniciarSesion(email, contrasenia)
            }
        }

        btnRegistrar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registroFragment)
        }

        // Observa los cambios en el perfil desde el ViewModel
        perfilViewModel.perfil.observe(viewLifecycleOwner) { perfil ->
            // Aquí puedes manejar el perfil si es necesario
        }
    }

    private fun iniciarSesion(email: String, contrasenia: String) {
        btnLogin.isEnabled = false
        auth.signInWithEmailAndPassword(email, contrasenia)
            .addOnCompleteListener(requireActivity()) { task ->
                btnLogin.isEnabled = true
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    // Descarga el perfil usando el ID del usuario
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        perfilViewModel.descargarPerfil(userId)
                    }

                    // Navega a PerfilFragment o donde sea necesario
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "El correo electrónico no está registrado."
                        is FirebaseAuthInvalidCredentialsException -> "La contraseña es incorrecta."
                        is FirebaseAuthException -> "Error: ${task.exception?.message}"
                        else -> "Error desconocido."
                    }
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                btnLogin.isEnabled = true
                Log.e("LoginFragment", "Error during login", exception)
                Toast.makeText(requireContext(), "Error en la autenticación. Intenta nuevamente.", Toast.LENGTH_SHORT).show()
            }
    }
}
