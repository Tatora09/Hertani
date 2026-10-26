# Hertani - Aplicación Móvil para Selección y Evaluación WIM 100

## Descripción del Proyecto
**Hertani** es una aplicación móvil desarrollada en Android Studio con Kotlin y Jetpack Compose para la gestión y evaluación de candidaturas en el contexto de minería e industria (WIM 100). Integra formularios con validaciones visuales en tiempo real, persistencia de datos local con Room Database, arquitectura modular desacoplada (MVVM) e integración con recursos nativos del dispositivo Android.

## Integrantes
- Nicolás Herrera
- Renato Qevedo
- Hernaldo Silva
- **Asignatura**: Desarrollo de Aplicaciones Móviles (DSY1105)

## Funcionalidades Principales
- **Autenticación y Gestión de Roles**: Inicio de sesión con diferenciación de perfiles (Evaluadora y Dirección).
- **Formularios Validados**: Registro de candidaturas y matrices de evaluación con validación en tiempo real por campo, límites de caracteres, restricción a dominios de prueba (`@hertani.test`, `@example.com`) y retroalimentación visual inmediata con mensajes de error e íconos.
- **Navegación Fluida y Animada**: Implementación de Jetpack Navigation Compose con transiciones animadas entre vistas.
- **Persistencia Local con Room**: Gestión de datos SQLite con Room Database (`HertaniDatabase`), incluyendo entidades, DAOs, TypeConverters y precarga de datos semilla en segundo plano.
- **Recursos Nativos del Dispositivo**:
  1. **Selector de Documentos del Sistema (Storage Access Framework - SAF)**: Selección segura de archivos PDF (CV de la candidata) mediante `ActivityResultContracts.OpenDocument()`, leyendo metadatos como nombre y tamaño sin requerir permisos invasivos.
  2. **Intents Implícitos del Sistema (`ACTION_VIEW` y `ACTION_SEND`)**: Apertura del perfil profesional en el navegador del dispositivo y función para compartir el resumen/resultado consolidado de la candidatura hacia otras aplicaciones del sistema (WhatsApp, Correo, etc.).
- **Trazabilidad e Historial**: Registro de eventos y auditoría de acciones realizadas dentro de la aplicación.

## Requisitos y Pasos para Ejecutar
1. Clonar el repositorio: `git clone https://github.com/Tatora09/Hertani.git`
2. Abrir el proyecto en **Android Studio (Ladybug o superior)**.
3. Sincronizar Gradle con los archivos del proyecto (`Sync Project with Gradle Files`).
4. Seleccionar un emulador o dispositivo físico con **Android 7.0 (API 24) o superior**.
5. Ejecutar la aplicación presionando `Run 'app'`.
