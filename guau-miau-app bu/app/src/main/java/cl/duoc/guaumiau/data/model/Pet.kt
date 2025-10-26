package cl.duoc.guaumiau.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * IL2.3: Modelo de datos para Mascota
 */
@Entity(tableName = "pets")
data class Pet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val name: String,
    val type: PetType,
    val photoUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Tipos de mascotas permitidos
 */
enum class PetType(val displayName: String) {
    DOG("Perro"),
    CAT("Gato"),
    BIRD("Ave"),
    OTHER("Otro")
}

/**
 * Estado del formulario de registro de mascota
 */
data class PetFormState(
    val name: String = "",
    val nameError: String? = null,
    
    val type: PetType? = null,
    val typeError: String? = null,
    
    val photoUri: String? = null,
    
    val isValid: Boolean = false
)
