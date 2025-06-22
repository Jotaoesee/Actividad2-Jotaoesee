👤 Gestor de Perfiles Firebase para Android 🚀
Descripción del Proyecto
Gestor de Perfiles Firebase para Android es una aplicación móvil nativa desarrollada en Kotlin para la plataforma Android. Su propósito principal es demostrar y gestionar el ciclo de vida completo de los perfiles de usuario, incluyendo autenticación, creación, visualización, edición y eliminación de datos. La aplicación se integra profundamente con los servicios de Firebase (Authentication y Firestore) para manejar la lógica de backend, ofreciendo una solución robusta y escalable para la gestión de usuarios.

La aplicación presenta una interfaz de usuario clara y estructurada, con pantallas dedicadas para el inicio de sesión, registro, edición de perfil y un directorio de usuarios, todo ello orquestado mediante el componente de navegación de Android.

El Problema que Resuelve
Gestor de Perfiles Firebase para Android aborda la necesidad de:

Un sistema de autenticación de usuarios funcional: Permite a los usuarios registrarse e iniciar sesión de forma segura.

Gestión de datos de perfil persistente: Almacena y recupera información del perfil de usuario de manera confiable en la nube (Firestore).

Demostración de interacción con Firebase: Sirve como un ejemplo práctico de cómo integrar y utilizar Firebase Auth y Firestore en una aplicación Android Kotlin.

Componentes de UI esenciales: Muestra el uso de RecyclerView para listas, EditText para formularios y el Componente de Navegación para la fluidez entre pantallas.

¿Para Quién es Útil?
Este proyecto es ideal para:

Desarrolladores Android (Kotlin): Que buscan un ejemplo completo y bien estructurado de una aplicación que utiliza Firebase para la gestión de usuarios.

Estudiantes y Aprendices: Interesados en comprender la integración de Firebase en Android, la arquitectura de Fragmentos y la navegación.

Equipos de Desarrollo: Como base para construir aplicaciones que requieran funcionalidades de gestión de perfiles y autenticación en Android.

✨ Características Destacadas
🔐 Autenticación Completa con Firebase Authentication:

Registro de Usuarios: Permite a los nuevos usuarios crear cuentas con email y contraseña.

Inicio de Sesión: Autentica a los usuarios existentes.

Cierre de Sesión: Facilita la finalización segura de la sesión del usuario.

Manejo de errores específico para la autenticación (ej., credenciales incorrectas, email ya en uso).

👤 Gestión Integral de Perfiles de Usuario:

Creación y Edición: Los usuarios pueden guardar y actualizar su nombre, apellidos, edad y número de teléfono en Firestore.

Recuperación de Datos: Muestra el perfil del usuario autenticado en una pantalla dedicada.

Eliminación de Perfil: Permite a los usuarios eliminar sus propios datos de perfil de Firestore.

Los datos se almacenan en la colección "users" de Firestore, utilizando el UID del usuario como ID del documento.

📊 Directorio de Usuarios:

Muestra una lista de todos los perfiles de usuario registrados en la aplicación mediante un RecyclerView.

Utiliza un ProfileAdapter para una visualización eficiente de la lista.

🚀 Flujo de Navegación Intuitivo:

Implementa una pantalla de Splash (SplashFragment) que verifica automáticamente el estado de autenticación del usuario y redirige al flujo de login/registro o directamente a la pantalla de perfil si ya está logueado.

Utiliza el Componente de Navegación de Android para transiciones fluidas entre LoginFragment, RegistroFragment, PerfilFragment y ProfilesFragment.

La HomeActivity actúa como contenedor principal de los fragmentos post-login.

🛠️ Tecnologías Utilizadas
Lenguaje de Programación: Kotlin

Plataforma: Android

Backend como Servicio (BaaS): Google Firebase

Firebase Authentication: Para la gestión de la autenticación de usuarios.

Cloud Firestore: Base de datos NoSQL para almacenar y recuperar los datos de perfil de los usuarios.

Componentes Android: Fragments, RecyclerView, EditText, Button, Toast.

Librería de Navegación: Android Jetpack Navigation Component.

Patrones de Diseño: Uso de Singleton (DataHolder) para la gestión centralizada de datos.

🚀 Cómo Instalar y Ejecutar
Para ejecutar Gestor de Perfiles Firebase para Android en tu entorno local, necesitarás Android Studio y un proyecto de Firebase configurado.

Prerrequisitos
Android Studio: Entorno de desarrollo recomendado.

Un dispositivo Android o emulador para probar la aplicación.

Una Cuenta de Google y un Proyecto de Firebase:

Crea un proyecto en la Consola de Firebase.

Configura tu aplicación Android con Firebase siguiendo la documentación oficial.

Habilita los servicios de Authentication (Email/Password) y Firestore Database.

Asegúrate de configurar las reglas de seguridad de Firestore para permitir las operaciones de lectura/escritura necesarias para los usuarios autenticados (ejemplo básico para pruebas: allow read, write: if request.auth != null;).

Pasos de Instalación
Clona el repositorio:

git clone https://github.com/tu_usuario/gestor_perfiles_firebase_android.git
cd gestor_perfiles_firebase_android

(Nota: Reemplaza https://github.com/tu_usuario/gestor_perfiles_firebase_android.git con la URL real de tu repositorio y gestor_perfiles_firebase_android con el nombre de tu proyecto en tu máquina si es diferente.)

Abre el proyecto en Android Studio.

Configuración de Firebase para Android:

Asegúrate de que el archivo google-services.json de tu proyecto de Firebase esté ubicado en el directorio app/. Si no lo tienes, descárgalo de la consola de Firebase y colócalo allí.

Cómo Ejecutar la Aplicación
En Android Studio, selecciona un dispositivo o emulador Android.

Haz clic en el botón Run (el triángulo verde) en la barra de herramientas.

La aplicación se construirá e instalará en tu dispositivo/emulador.

📈 Cómo Usar la Aplicación
Gestor de Perfiles Firebase para Android te guía a través de un flujo sencillo de autenticación y gestión de perfil.

Flujo de Usuario
Pantalla de Splash: Al iniciar la aplicación, verás una pantalla de carga breve.

Inicio de Sesión / Registro:

Si no has iniciado sesión, serás dirigido al LoginFragment. Aquí puedes ingresar tus credenciales o navegar al RegistroFragment para crear una nueva cuenta.

Tras un inicio de sesión o registro exitoso, la aplicación intentará cargar tu perfil.

Gestión de Perfil (PerfilFragment):

Si tu perfil existe, verás tus datos. Puedes editarlos y guardarlos, o incluso eliminar tu perfil.

Si no tienes un perfil (ej., eres un nuevo usuario registrado), se te presentará una pantalla para crear tu perfil.

Directorio de Perfiles (ProfilesFragment - Contenido de HomeActivity):

Una vez que tu sesión está activa, la HomeActivity cargará el ProfilesFragment, donde podrás ver una lista de todos los perfiles de usuario registrados en la aplicación.

Desde aquí, también puedes cerrar tu sesión para volver a la pantalla de login.
