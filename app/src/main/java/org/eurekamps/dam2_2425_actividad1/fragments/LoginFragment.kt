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
        // Inflar el layout del fragmento de inicio de sesión
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

            //Compruebo que los campos no esten vacios
            if (email.isEmpty() || contrasenia.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                iniciarSesion(email, contrasenia)
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
                    findNavController().navigate(R.id.action_loginFragment_to_perfilFragment)
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
