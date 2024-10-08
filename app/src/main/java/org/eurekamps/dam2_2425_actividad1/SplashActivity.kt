package org.eurekamps.dam2_2425_actividad1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import org.eurekamps.dam2_2425_actividad1.fragments.LoginFragment
import org.eurekamps.dam2_2425_actividad1.fragments.PerfilFragment

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Usar un handler para retrasar la navegación
        Handler().postDelayed({
            // Verificar si el usuario está autenticado
            val user = auth.currentUser
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
            finish()
        }, 2000) // 2000 milisegundos (2 segundos) de retraso
    }
}
