package cl.duoc.guaumiau.utils

import java.util.regex.Pattern

/**
 * IL2.1: Utilidades de validación de formularios
 * Validaciones según especificaciones del proyecto
 */
object ValidationUtils {
    
    /**
     * Valida el nombre completo
     * - No debe estar vacío
     * - Solo caracteres alfabéticos y espacios
     * - Máximo 50 caracteres
     */
    fun validateFullName(name: String): String? {
        return when {
            name.isBlank() -> "El nombre no puede estar vacío"
            !name.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) -> 
                "El nombre solo puede contener letras y espacios"
            name.length > 50 -> "El nombre no puede exceder 50 caracteres"
            else -> null
        }
    }
    
    /**
     * Valida el correo electrónico
     * - Debe seguir formato estándar
     * - Solo acepta dominios @duoc.cl
     */
    fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "El correo no puede estar vacío"
            !email.endsWith("@duoc.cl") -> "Solo se aceptan correos @duoc.cl"
            !isValidEmailFormat(email) -> "Formato de correo inválido"
            else -> null
        }
    }
    
    private fun isValidEmailFormat(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
            "@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
        )
        return emailPattern.matcher(email).matches()
    }
    
    /**
     * Valida la contraseña
     * - Mínimo 8 caracteres
     * - Al menos una mayúscula
     * - Al menos una minúscula
     * - Al menos un número
     * - Al menos un carácter especial (@#$%&*)
     */
    fun validatePassword(password: String): String? {
        return when {
            password.length < 8 -> "La contraseña debe tener al menos 8 caracteres"
            !password.any { it.isUpperCase() } -> "Debe incluir al menos una mayúscula"
            !password.any { it.isLowerCase() } -> "Debe incluir al menos una minúscula"
            !password.any { it.isDigit() } -> "Debe incluir al menos un número"
            !password.any { it in "@#$%&*" } -> "Debe incluir al menos un carácter especial (@#$%&*)"
            else -> null
        }
    }
    
    /**
     * Valida que las contraseñas coincidan
     */
    fun validatePasswordMatch(password: String, confirmPassword: String): String? {
        return if (password != confirmPassword) {
            "Las contraseñas no coinciden"
        } else {
            null
        }
    }
    
    /**
     * Valida el nombre de la mascota
     * - No debe estar vacío
     * - Máximo 50 caracteres
     */
    fun validatePetName(name: String): String? {
        return when {
            name.isBlank() -> "El nombre de la mascota no puede estar vacío"
            name.length > 50 -> "El nombre no puede exceder 50 caracteres"
            else -> null
        }
    }
}
