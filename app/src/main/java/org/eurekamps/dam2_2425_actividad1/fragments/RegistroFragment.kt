package org.eurekamps.dam2_2425_actividad1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import org.eurekamps.dam2_2425_actividad1.R




class RegistroFragment : Fragment() {

    lateinit var txEmail: EditText
    lateinit var txContraseña: EditText
    lateinit var txConfirmarContraseña: EditText
    lateinit var btnVolver: Button
    lateinit var btnRegistrar: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Inicializar las vistas aquí
        txEmail = view.findViewById(R.id.txEmail)
        txContraseña = view.findViewById(R.id.txContraseña)
        txConfirmarContraseña = view.findViewById(R.id.txConfirmarContraseña)
        btnVolver = view.findViewById(R.id.btnVolver)
        btnRegistrar = view.findViewById(R.id.btnRegistrar)

        // Aquí añado listeners o cualquier lógica de manejo de eventos
        btnRegistrar.setOnClickListener {

        }

        btnVolver.setOnClickListener {
            findNavController().navigate(R.id.action_registroFragment_to_loginFragment)
        }
    }

    companion object {


    }
}