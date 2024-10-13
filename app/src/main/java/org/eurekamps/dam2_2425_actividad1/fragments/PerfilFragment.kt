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


class PerfilFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    lateinit var txNombre : EditText
    lateinit var txApellidos : EditText
    lateinit var txEdad : EditText
    lateinit var txNumero : EditText
    lateinit var txBiografia : EditText
    lateinit var btnCerrarSesion : Button
    lateinit var btnEditarPerfil : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txNombre = view.findViewById(R.id.txNombrePerfil)
        txApellidos = view.findViewById(R.id.txApellidosPerfil)
        txEdad = view.findViewById(R.id.txEdadPerfil)
        txNumero = view.findViewById(R.id.txNumeroPerfil)
        txBiografia = view.findViewById(R.id.txBiografiaPerfil)
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion)
        btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil)


        btnCerrarSesion.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_perfilFragment_to_loginFragment)
        }


        btnEditarPerfil.setOnClickListener {

       }


    }

}