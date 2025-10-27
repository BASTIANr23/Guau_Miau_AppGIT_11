Guau&Miau - Aplicación Android Veterinaria
Pasos para Ejecutar
Abrir en Android Studio

File → Open → Selecciona la carpeta del proyecto
Sincronizar Gradle

Espera a que Android Studio sincronice automáticamente
Si no, haz clic en "Sync Now"
Crear Emulador (si no tienes uno)

Tools → Device Manager → Create Device
Selecciona: Pixel 6
System Image: Android 13 (API 33) o superior
Ejecutar

Selecciona el emulador en la barra superior
Haz clic en ▶️ Run o presiona Shift + F10
✨ Nuevas Funcionalidades Implementadas
🎨 Animaciones y UX Mejorada
AnimatedComponents: Componentes con transiciones suaves
AnimatedButton: Botones con efecto de escala al presionar
AnimatedCard: Cards con animación de entrada
AnimatedErrorMessage: Mensajes de error con transiciones
📸 Recursos Nativos
Captura de Fotos: Integración completa con cámara y galería
Notificaciones: Sistema de notificaciones locales
Manejo de Permisos: Gestión automática de permisos de cámara y notificaciones
🐾 Gestión Completa de Mascotas
Edición de Mascotas: Pantalla completa para editar información
Fotos de Mascotas: Captura y almacenamiento de fotos
Validaciones Mejoradas: Retroalimentación visual en tiempo real
🎯 Validaciones y Formularios
Validaciones Visuales: Mensajes de error animados
Formularios Reactivos: Validación en tiempo real
Retroalimentación Visual: Íconos y colores según estado
Estructura del Proyecto
app/src/main/java/cl/duoc/guaumiau/
data/ - Modelos, DAOs, Repositorios
ui/screens/ - Pantallas de la app
ui/components/ - NUEVO: Componentes reutilizables
navigation/ - Sistema de navegación
utils/ - Utilidades y validaciones
Pantallas Disponibles
HomeScreen - Pantalla de bienvenida
LoginScreen - Inicio de sesión con validaciones mejoradas
RegisterScreen - Registro de usuario
PetsRegistrationScreen - Registro de mascotas con fotos
EditPetScreen - NUEVA: Edición completa de mascotas
CatalogScreen - Catálogo de productos con animaciones
ProductDetailScreen - Detalle de producto
ProfileScreen - Perfil de usuario mejorado
🔧 Tecnologías y Librerías
100% Kotlin - Lenguaje principal
Jetpack Compose - UI moderna y declarativa
Room Database - Persistencia local
DataStore - Almacenamiento de preferencias
MVVM Architecture - Arquitectura limpia
Navigation Compose - Navegación entre pantallas
CameraX - Captura de fotos
Coil - Carga de imágenes
Accompanist Permissions - Manejo de permisos
Material 3 - Diseño moderno
📱 Recursos Nativos Implementados
1. Cámara y Galería
Captura de fotos con CameraX
Selección desde galería
FileProvider para compartir archivos
Manejo automático de permisos
2. Notificaciones
Notificaciones locales
Canales de notificación
Permisos dinámicos (Android 13+)
Notificaciones contextuales
🎨 Características de Diseño
Material 3 Design
Colores dinámicos y temáticos
Componentes modernos
Tipografía consistente
Elevaciones y sombras
Animaciones Fluidas
Transiciones entre pantallas
Animaciones de entrada/salida
Efectos de retroalimentación
Indicadores de carga animados
Validaciones Visuales
Mensajes de error contextuales
Íconos de estado
Colores semánticos
Retroalimentación inmediata
🔒 Permisos Requeridos
CAMERA - Para captura de fotos
READ_EXTERNAL_STORAGE - Para acceso a galería (API < 33)
READ_MEDIA_IMAGES - Para acceso a imágenes (API 33+)
POST_NOTIFICATIONS - Para notificaciones (API 33+)
INTERNET - Para carga de imágenes remotas
🚀 Próximas Mejoras Sugeridas
 Sincronización en la nube
 Recordatorios de citas veterinarias
 Chat con veterinarios
 Historial médico de mascotas
 Geolocalización de veterinarias
