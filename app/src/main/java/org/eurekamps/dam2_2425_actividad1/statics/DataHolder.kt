package org.eurekamps.dam2_2425_actividad1.statics

import android.app.Activity

import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.eurekamps.dam2_2425_actividad1.fbClases.FbProfile

object DataHolder {
    var perfil: FbProfile? = null
    val TAG="DataHolder"

    fun descargarPerfil(miActivity: Activity, miNav: NavController, idActionNav:Int, idActionNavNull:Int){
        val db = Firebase.firestore
        val auth = FirebaseAuth.getInstance()
        val docRef = db.collection("users").document(auth.currentUser!!.uid)
        docRef.get().addOnSuccessListener { document ->
            if (document.data != null) {
                perfil = document.toObject(FbProfile::class.java)

                Log.d(TAG, "DocumentSnapshot data: ${document.data!!}")
                Log.d(TAG, "Mi Perfil data NAME: ${perfil!!.nombre}")

                miNav.navigate(idActionNav)

            } else {
                miNav.navigate(idActionNavNull)
                Log.d(TAG, "No existe el documento con los datos del perfil para esta ID")
            }
        }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }
}