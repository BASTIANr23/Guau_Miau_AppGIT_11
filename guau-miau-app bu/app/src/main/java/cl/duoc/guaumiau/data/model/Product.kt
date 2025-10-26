package cl.duoc.guaumiau.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * IL2.3: Modelo de datos para productos con persistencia local
 */
@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: ProductCategory,
    val petType: PetType,
    val imageUrl: String,
    val isSustainable: Boolean = false,
    val isInnovative: Boolean = false,
    val stock: Int = 10,
    val rating: Float = 4.5f,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ProductCategory(val displayName: String) {
    DOG_TOYS("Juguetes para Perros"),
    CAT_TOYS("Juguetes para Gatos"),
    BIRD_TOYS("Juguetes para Aves"),
    INTERACTIVE("Juguetes Interactivos"),
    SUSTAINABLE("Juguetes Sostenibles"),
    ALL("Todos")
}
