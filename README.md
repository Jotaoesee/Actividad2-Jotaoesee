[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/yWiL0C6L)
# Actividad 1: PMDM + PSP + AD
Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)

## Método de entrega

Todos los trabajos se entregarán usando GIT y el enlace proporcionado a continuación.  
Debes aceptar la invitación y clonar el repositorio de GitHub Classroom que se haya creado en tu cuenta de GitHub.  
Todas las entregas deberán **AL MENOS** tener 35 commits, por lo que se recomienda ir haciendo commits con frecuencia para marcar los cambios realizados.  
En el aula virtual y en el canal de YouTube "Eurekamps", hay videos tutoriales de las clases que puedes usar en el proceso de desarrollo.

## Enunciado

### Desarrollo:

1. **Crear una aplicación usando Android Kotlin.** (05 puntos)
2. **Crear los fragmentos y ficheros de navegación** para simular la activity de autenticación. Conectar el proyecto con Firebase. (1 punto)
3. **Diseñar las pantallas** de la aplicación para simular el registro y login de una app móvil en Android. Se deben seguir las buenas prácticas de desarrollo, como la creación de valores estáticos en los `Strings.xml`, incluyendo colores, entre otros. (2 puntos)
4. **Implementar el registro y login con Firebase**. Deben contemplarse las restricciones para la contraseña y el formato del correo electrónico. En caso de error, se debe notificar al usuario visualmente (por ejemplo, usando un Toast). (2 puntos)
5. **Pantalla Splash y perfil**:
    - Crear una pantalla Splash que determine si el usuario está logueado o no. (05 puntos)
    - Crear un `FragmentPerfil` donde el usuario puede agregar información que será subida a la base de datos. Se deben contemplar los siguientes casos:
        - No logueado y sin perfil.
        - Logueado y sin perfil.
        - No logueado y con perfil.
        - Logueado con perfil.
        - No registrado. (2 puntos)
6. **Insertar el perfil en la base de datos** desde la pantalla de perfil. Usa `Data Classes` para representar el objeto `Perfil` que será descargado y subido.
7. **Splash Screen**: Debe contener una imagen y un marcador de progreso de carga (barra o circular). (05 puntos)
8. **Crear un `DataHolder` estático** (usando `Companion Object`) que almacene las variables que apuntan al objeto `Perfil`. Esto permitirá acceder a los datos del perfil desde cualquier `Activity` o `Fragment`, como en el `HomeActivity`. (1 punto)
9. **HomeActivity**: Mostrar un `Fragmento` llamado `ProfilesFragment` que contenga una lista de todos los perfiles de la colección `Profiles` en formato lista. (1 punto)

## Notas:
Este proceso es el mismo que se está realizando en los tutoriales de clase, por lo que se recomienda ver los videos paso a paso.

