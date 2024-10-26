package org.eurekamps.dam2_2425_actividad1.fragmentsMain

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import org.eurekamps.dam2_2425_actividad1.HomeActivity
import org.eurekamps.dam2_2425_actividad1.R
import org.eurekamps.dam2_2425_actividad1.viewModelMain.SplashViewModel

class SplashFragment : Fragment() {

    private val SplashViewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Usar un Handler para retrasar la ejecución y mostrar el Splash por 3 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            // Verificar la autenticación del usuario
            SplashViewModel.verificarAutenticacionUsuario()
        }, 3000) // 3 segundos de espera

        // Observa el estado de autenticación
        SplashViewModel.usuarioAutenticado.observe(viewLifecycleOwner) { esAutenticado ->
            if (esAutenticado) {
                // Si el usuario ya está autenticado, navega al HomeActivity
                Log.d("SplashFragment", "Usuario autenticado")
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                // Si no hay usuario autenticado, navega al LoginFragment
                Log.d("SplashFragment", "No hay usuario autenticado.")
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }
    }
}
