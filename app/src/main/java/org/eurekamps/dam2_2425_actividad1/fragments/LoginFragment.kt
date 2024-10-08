package org.eurekamps.dam2_2425_actividad1.fragments

import android.os.Bundle
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar las vistas aquí
        txEmail = view.findViewById(R.id.txEmail)
        txContraseña = view.findViewById(R.id.txContraseña)
        txInicioSesion = view.findViewById(R.id.txInicio)
        btnLogin = view.findViewById(R.id.btnLogin)
        btnRegistrar = view.findViewById(R.id.btnRegistrar)

        // Listener para el botón de login
        btnLogin.setOnClickListener {
            val email = txEmail.text.toString().trim()
            val contraseña = txContraseña.text.toString().trim()

            // Verificar si los campos están vacíos
            if (email.isEmpty() || contraseña.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                // Iniciar sesión con Firebase
                iniciarSesion(email, contraseña)
            }
        }

        // Listener para el botón de registro
        btnRegistrar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registroFragment)
        }
    }

    private fun iniciarSesion(email: String, contraseña: String) {
        auth.signInWithEmailAndPassword(email, contraseña)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso y navego al perfilFragment
                    Toast.makeText(requireContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_perfilFragment)
                } else {
                    // Si ocurre un error en el inicio de sesión
                    Toast.makeText(requireContext(), "Error en el inicio de sesión: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}
