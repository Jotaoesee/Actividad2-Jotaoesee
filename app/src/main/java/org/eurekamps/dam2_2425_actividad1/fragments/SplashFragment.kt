package org.eurekamps.dam2_2425_actividad1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Muestra el SplashFragment durante 3 segundos
        Handler().postDelayed({
            val intent = Intent(activity, HomeActivity::class.java)
            startActivity(intent)
            activity?.finish() // Cierra MainActivity para evitar volver al splash
        }, 3000) // 3000 ms = 3 segundos
    }
}
