# MPAZ PRO 3.º – App Escuela Escritora Marcela Paz

Aplicación móvil Android de apoyo didáctico y seguimiento del aprendizaje para estudiantes de Tercero Básico de la Escuela Escritora Marcela Paz (San Bernardo). MVP académico desarrollado para la asignatura DSY1105 – Desarrollo de Aplicaciones Móviles (Duoc UC).

Todos los datos utilizados (estudiantes, resultados e informes) son ficticios.

## Integrantes
- Anita Olivares

## Funcionalidades implementadas
- **Perfil de usuario:** nombre del estudiante y foto de perfil desde **galería** o **cámara** (recursos nativos).
- **Control de acceso por rol:** Estudiante o Docente/UTP, con clave de autorización validada.
- **Registro de desafío:** selección de asignatura (Lenguaje, Matemática, Ciencias Naturales, Historia), registro de respuestas correctas y control de intentos máximos.
- **Validación de formularios** con mensajes de error por campo e íconos.
- **Informe pedagógico UTP:** visible solo para el rol Docente/UTP, con aciertos, barra de progreso y estado del logro.
- **Persistencia local** con DataStore Preferences (rol, perfil, intentos y resultados se conservan al cerrar la app).
- **Animaciones** de retroalimentación (resultado del desafío con AnimatedVisibility).
- **Navegación** con Navigation Compose, TopAppBar y NavigationBar.

## Arquitectura
Patrón **MVVM**:
- `model/` – data classes de estado y errores.
- `ui/screen/` – pantallas Composable.
- `ui/viewmodel/` – lógica de presentación con StateFlow.
- `data/` – Repository de persistencia local (DataStore).
- `navigation/` – rutas (sealed class) y NavHost.
- `utils/` – validaciones reutilizables.

## Tecnologías
Kotlin · Jetpack Compose · Material 3 · Navigation Compose · DataStore Preferences · Coil · Activity Result API

## Pasos para ejecutar
1. Clonar el repositorio:
   `git clone https://github.com/aniolivares/AppEscuelaEscritoraMarcelaPaz.git`
2. Abrir la carpeta en Android Studio y esperar la sincronización de Gradle.
3. Crear o seleccionar un emulador (Device Manager) o conectar un dispositivo Android con depuración USB.
4. Presionar **Run ▶**.
5. Para acceder al rol Docente/UTP, usar la clave de prueba indicada por el equipo.

## Planificación
Tablero Trello: https://trello.com/b/ur2cwYsr/appescuelampazanitaolivares