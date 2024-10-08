package org.eurekamps.dam2_2425_actividad1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import org.eurekamps.dam2_2425_actividad1.R

class RegistroFragment : Fragment() {

    lateinit var txEmail: EditText
    lateinit var txContraseña: EditText
    lateinit var txConfirmarContraseña: EditText
    lateinit var btnVolver: Button
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
        return inflater.inflate(R.layout.fragment_registro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializo las vistas aquí
        txEmail = view.findViewById(R.id.txEmailRegistro)
        txContraseña = view.findViewById(R.id.txContraseñaRegistro)
        txConfirmarContraseña = view.findViewById(R.id.txConfirmarContraseñaRegistro)
        btnVolver = view.findViewById(R.id.btnVolverRegistro)
        btnRegistrar = view.findViewById(R.id.btnRegistrarRegistro)

        // Listener para el botón de registrar
        btnRegistrar.setOnClickListener {
            val email = txEmail.text.toString().trim()
            val contraseña = txContraseña.text.toString().trim()
            val confirmarContraseña = txConfirmarContraseña.text.toString().trim()

            // Validar los campos antes de intentar el registro
            if (email.isEmpty() || contraseña.isEmpty() || confirmarContraseña.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show()
            } else if (contraseña != confirmarContraseña) {
                Toast.makeText(requireContext(), "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show()
            } else {
                // Llamar a la función para registrar al usuario
                registrarUsuario(email, contraseña)
            }
        }

        // Listener para el botón de volver
        btnVolver.setOnClickListener {
            findNavController().navigate(R.id.action_registroFragment_to_loginFragment)
        }
    }

    // Función para registrar al usuario en Firebase
    private fun registrarUsuario(email: String, contraseña: String) {
        auth.createUserWithEmailAndPassword(email, contraseña)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Registro exitoso
                    Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
                    // Navegar al login u otra pantalla
                    findNavController().navigate(R.id.action_registroFragment_to_loginFragment)
                } else {
                    // Si ocurre un error, mostrar el mensaje
                    Toast.makeText(requireContext(), "Error en el registro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
