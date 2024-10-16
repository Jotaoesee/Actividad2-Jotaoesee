package org.eurekamps.dam2_2425_actividad1.fragments

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

    lateinit var txEmail: EditText
    lateinit var txContraseña: EditText
    lateinit var txInicioSesion: TextView
    lateinit var btnLogin: Button
    lateinit var btnRegistrar: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar FirebaseAuth
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

        // Inicializo las vistas aquí
        txEmail = view.findViewById(R.id.txEmailRegistro)
        txContraseña = view.findViewById(R.id.txContraseñaRegistro)
        txInicioSesion = view.findViewById(R.id.txInicio)
        btnLogin = view.findViewById(R.id.btnLogin)
        btnRegistrar = view.findViewById(R.id.btnRegistrarRegistro)


        // Listener para el botón de login
        btnLogin.setOnClickListener {
            Log.d("LoginFragment", "Login button clicked")
            val email = txEmail.text.toString().trim()
            val contrasenia = txContraseña.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, introduce un correo electrónico.", Toast.LENGTH_SHORT).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(requireContext(), "Formato de correo electrónico inválido.", Toast.LENGTH_SHORT).show()
            } else if (contrasenia.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, introduce una contraseña.", Toast.LENGTH_SHORT).show()
            } else {
                iniciarSesion(email, contrasenia) // Llama a la función si la validación es correcta
            }
        }

        // Listener para el botón de registro
        btnRegistrar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registroFragment)
        }
    }

    //Función para iniciar sesión
    private fun iniciarSesion(email: String, contrasenia: String) {
        auth.signInWithEmailAndPassword(email, contrasenia)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso y navego al perfilFragment
                    Toast.makeText(requireContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    //findNavController().navigate(R.id.action_loginFragment_to_perfilFragment)
                    DataHolder.descargarPerfil(requireActivity(),findNavController(),R.id.action_loginFragment_to_profilesFragment,R.id.action_loginFragment_to_perfilFragment)
                } else {
                    // Manejo de errores
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "El correo electrónico no está registrado."
                        is FirebaseAuthInvalidCredentialsException -> "La contraseña es incorrecta."
                        is FirebaseAuthException -> "Error: ${task.exception?.message}"
                        else -> "Error desconocido."
                    }
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }


}
