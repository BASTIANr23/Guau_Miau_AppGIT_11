package cl.duoc.guaumiau.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * IL2.3: Modelo de datos para el carrito de compras
 */
@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val productId: Int,
    val quantity: Int = 1,
    val addedAt: Long = System.currentTimeMillis()
)

/**
 * Modelo para mostrar items del carrito con información del producto
 * Clase plana para facilitar el mapeo de Room
 */
data class CartItemWithProduct(
    // Campos del CartItem
    val id: Int,
    val userId: Int,
    val productId: Int,
    val quantity: Int,
    val addedAt: Long,
    
    // Campos del Product
    val productName: String,
    val productDescription: String,
    val productPrice: Double,
    val productCategory: ProductCategory,
    val productPetType: PetType,
    val productImageUrl: String,
    val productIsSustainable: Boolean,
    val productIsInnovative: Boolean,
    val productStock: Int,
    val productRating: Float
) {
    val totalPrice: Double
        get() = productPrice * quantity
    
    // Propiedades de conveniencia para acceder a los datos como objetos separados
    val cartItem: CartItem
        get() = CartItem(id, userId, productId, quantity, addedAt)
    
    val product: Product
        get() = Product(
            id = productId,
            name = productName,
            description = productDescription,
            price = productPrice,
            category = productCategory,
            petType = productPetType,
            imageUrl = productImageUrl,
            isSustainable = productIsSustainable,
            isInnovative = productIsInnovative,
            stock = productStock,
            rating = productRating
        )
}

/**
 * Estado del carrito
 */
data class CartState(
    val items: List<CartItemWithProduct> = emptyList(),
    val totalItems: Int = 0,
    val totalPrice: Double = 0.0,
    val isLoading: Boolean = false
) {
    companion object {
        fun fromItems(items: List<CartItemWithProduct>): CartState {
            return CartState(
                items = items,
                totalItems = items.sumOf { it.cartItem.quantity },
                totalPrice = items.sumOf { it.totalPrice }
            )
        }
    }
}

/**
 * Resultado de operaciones del carrito
 */
sealed class CartResult {
    object Success : CartResult()
    data class Error(val message: String) : CartResult()
}