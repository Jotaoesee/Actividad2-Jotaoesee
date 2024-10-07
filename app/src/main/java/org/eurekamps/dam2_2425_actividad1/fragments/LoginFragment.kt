package org.eurekamps.dam2_2425_actividad1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.eurekamps.dam2_2425_actividad1.R



class LoginFragment : Fragment() {

    lateinit var txEmail: EditText
    lateinit var txContraseña: EditText
    lateinit var txInicioSesion: TextView
    lateinit var btnLogin: Button
    lateinit var btnRegistrar: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        // Inicializar las vistas aquí, usando el parámetro 'view' proporcionado por onViewCreated
        txEmail = view.findViewById(R.id.txEmail)
        txContraseña = view.findViewById(R.id.txContraseña)
        txInicioSesion = view.findViewById(R.id.txInicio)
        btnLogin = view.findViewById(R.id.btnLogin)
        btnRegistrar = view.findViewById(R.id.btnRegistrar)

        // Aquí añado listeners o cualquier lógica de manejo de eventos
        btnLogin.setOnClickListener {

        }

        btnRegistrar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registroFragment)
        }
    }

}
