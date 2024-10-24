package org.eurekamps.dam2_2425_actividad1.fbClases

// Clase de datos para representar un perfil de usuario en la aplicación
data class FbProfile(
    val nombre: String? = null,
    val apellidos: String? = null,
    val hobbies: String? = null,
    val imagenUrl: String? = null
) {
    // Constructor sin argumentos necesario para la deserialización
    constructor() : this(null, null, null, null)
}
