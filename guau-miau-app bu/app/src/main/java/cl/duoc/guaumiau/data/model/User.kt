package cl.duoc.guaumiau.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * IL2.3: Modelo de datos para Usuario
 * Almacenamiento local con Room Database
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fullName: String,
    val email: String,
    val password: String,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Estado de validación del formulario de registro
 */
data class RegisterFormState(
    val fullName: String = "",
    val fullNameError: String? = null,
    
    val email: String = "",
    val emailError: String? = null,
    
    val password: String = "",
    val passwordError: String? = null,
    
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    
    val isValid: Boolean = false
)

/**
 * Estado de validación del formulario de login
 */
data class LoginFormState(
    val email: String = "",
    val emailError: String? = null,
    
    val password: String = "",
    val passwordError: String? = null,
    
    val isValid: Boolean = false
)
