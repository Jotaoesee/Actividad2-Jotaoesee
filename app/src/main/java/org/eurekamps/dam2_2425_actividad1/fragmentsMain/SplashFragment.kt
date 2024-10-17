import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import org.eurekamps.dam2_2425_actividad1.R
import org.eurekamps.dam2_2425_actividad1.statics.DataHolder

class SplashFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Uso un Handler para retrasar la ejecución y mostrar el SplashScreen por unos segundos
        Handler(Looper.getMainLooper()).postDelayed({
            // Verificar si hay un usuario autenticado
            val usuario = auth.currentUser
            if (usuario != null) {
                // Usuario ya está autenticado, ir al PerfilFragment
                Log.d("SplashFragment", "Usuario autenticado: ${usuario.email}")
                DataHolder.descargarPerfil(requireActivity(),findNavController(),R.id.action_splashFragment_to_perfilFragment)

            } else {
                // No hay usuario autenticado, ir al LoginFragment
                Log.d("SplashFragment", "No hay usuario autenticado.")
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }, 3000) // Espera de 3 segundos antes de redirigir
    }
}
