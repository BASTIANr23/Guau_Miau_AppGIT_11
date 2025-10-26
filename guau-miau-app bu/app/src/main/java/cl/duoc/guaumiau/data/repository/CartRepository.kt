package cl.duoc.guaumiau.data.repository

import cl.duoc.guaumiau.GuauMiauApplication
import cl.duoc.guaumiau.data.local.CartDao
import cl.duoc.guaumiau.data.model.CartItem
import cl.duoc.guaumiau.data.model.CartItemWithProduct
import cl.duoc.guaumiau.data.model.CartResult
import kotlinx.coroutines.flow.Flow

/**
 * IL2.3: Repositorio para gestión del carrito de compras
 */
class CartRepository {
    
    private val cartDao: CartDao = GuauMiauApplication.instance.database.cartDao()
    
    fun getCartItems(userId: Int): Flow<List<CartItemWithProduct>> {
        return cartDao.getCartItemsWithProducts(userId)
    }
    
    fun getCartItemCount(userId: Int): Flow<Int> {
        return cartDao.getCartItemCount(userId)
    }
    
    fun getTotalQuantity(userId: Int): Flow<Int?> {
        return cartDao.getTotalQuantity(userId)
    }
    
    suspend fun addToCart(userId: Int, productId: Int, quantity: Int = 1): CartResult {
        return try {
            val existingItem = cartDao.getCartItem(userId, productId)
            
            if (existingItem != null) {
                // Si ya existe, actualizar cantidad
                val updatedItem = existingItem.copy(
                    quantity = existingItem.quantity + quantity
                )
                cartDao.updateCartItem(updatedItem)
            } else {
                // Si no existe, crear nuevo item
                val newItem = CartItem(
                    userId = userId,
                    productId = productId,
                    quantity = quantity
                )
                cartDao.insertCartItem(newItem)
            }
            CartResult.Success
        } catch (e: Exception) {
            CartResult.Error("Error al agregar al carrito: ${e.message}")
        }
    }
    
    suspend fun updateQuantity(cartItem: CartItem, newQuantity: Int): CartResult {
        return try {
            if (newQuantity <= 0) {
                cartDao.deleteCartItem(cartItem)
            } else {
                val updatedItem = cartItem.copy(quantity = newQuantity)
                cartDao.updateCartItem(updatedItem)
            }
            CartResult.Success
        } catch (e: Exception) {
            CartResult.Error("Error al actualizar cantidad: ${e.message}")
        }
    }
    
    suspend fun removeFromCart(cartItem: CartItem): CartResult {
        return try {
            cartDao.deleteCartItem(cartItem)
            CartResult.Success
        } catch (e: Exception) {
            CartResult.Error("Error al eliminar del carrito: ${e.message}")
        }
    }
    
    suspend fun clearCart(userId: Int): CartResult {
        return try {
            cartDao.clearCart(userId)
            CartResult.Success
        } catch (e: Exception) {
            CartResult.Error("Error al limpiar carrito: ${e.message}")
        }
    }
}