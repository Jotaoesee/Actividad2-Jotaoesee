package org.eurekamps.dam2_2425_actividad1.fbClases

// Clase de datos para representar un perfil de usuario en la aplicación
// Se utiliza en el adaptador del RecyclerView y en otros lugares donde se necesite un perfil
data class FbProfile(
    val nombre: String? = null,
    val apellidos: String? = null,
    val hobbies: String? = null,
    val imagenUrl: String?
)
