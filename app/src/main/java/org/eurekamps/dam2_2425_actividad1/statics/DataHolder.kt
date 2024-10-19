package org.eurekamps.dam2_2425_actividad1.statics

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.eurekamps.dam2_2425_actividad1.HomeActivity
import org.eurekamps.dam2_2425_actividad1.fbClases.FbProfile

object DataHolder {
    // Variable para mantener el perfil del usuario descargado, inicialmente nulo
    var perfil: FbProfile? = null
    // Etiqueta para la depuración
    val TAG = "DataHolder"

    // Función para descargar el perfil del usuario desde Firestore
    fun descargarPerfil(miActivity: Activity, miNav: NavController, idActionNav: Int) {
        // Inicializar la instancia de Firestore
        val db = Firebase.firestore
        // Inicializar la instancia de FirebaseAuth para acceder a la información del usuario autenticado
        val auth = FirebaseAuth.getInstance()
        // Obtener la referencia del documento del usuario autenticado en la colección "users"
        val docRef = db.collection("users").document(auth.currentUser!!.uid)

        // Realizar una consulta para obtener el documento
        docRef.get().addOnSuccessListener { document ->
            // Comprobar si el documento contiene datos
            if (document.data != null) {
                // Convertir el documento a un objeto de tipo FbProfile y asignarlo a la variable perfil
                perfil = document.toObject(FbProfile::class.java)

                // Mostrar en log los datos del documento obtenido
                Log.d(TAG, "DocumentSnapshot data: ${document.data!!}")
                // Mostrar en log el nombre del perfil
                Log.d(TAG, "Mi Perfil data NAME: ${perfil!!.nombre}")

                // Crear un Intent para iniciar HomeActivity
                val intentHomeActivity: Intent = Intent(miActivity, HomeActivity::class.java)
                // Iniciar HomeActivity
                miActivity.startActivity(intentHomeActivity)
                // Cerrar la actividad actual para que el usuario no vuelva a ella al presionar el botón atrás
                miActivity.finish()
            } else {
                // Si no hay datos, navegar a la acción especificada en idActionNav
                miNav.navigate(idActionNav)
                // Log para indicar que no se encontró el documento
                Log.d(TAG, "No existe el documento con los datos del perfil para esta ID")
            }
        }
            // Manejar el caso en que la solicitud a Firestore falle
            .addOnFailureListener { exception ->
                // Mostrar en log el error ocurrido
                Log.d(TAG, "get failed with ", exception)
            }
    }
}
