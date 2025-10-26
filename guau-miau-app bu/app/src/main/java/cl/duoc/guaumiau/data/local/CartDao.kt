package cl.duoc.guaumiau.data.local

import androidx.room.*
import cl.duoc.guaumiau.data.model.CartItem
import cl.duoc.guaumiau.data.model.CartItemWithProduct
import kotlinx.coroutines.flow.Flow

/**
 * IL2.3: DAO para operaciones del carrito de compras
 */
@Dao
interface CartDao {
    
    @Query("""
        SELECT cart_items.id,
               cart_items.userId,
               cart_items.productId,
               cart_items.quantity,
               cart_items.addedAt,
               products.name as productName,
               products.description as productDescription,
               products.price as productPrice,
               products.category as productCategory,
               products.petType as productPetType,
               products.imageUrl as productImageUrl,
               products.isSustainable as productIsSustainable,
               products.isInnovative as productIsInnovative,
               products.stock as productStock,
               products.rating as productRating
        FROM cart_items 
        INNER JOIN products ON cart_items.productId = products.id 
        WHERE cart_items.userId = :userId
        ORDER BY cart_items.addedAt DESC
    """)
    fun getCartItemsWithProducts(userId: Int): Flow<List<CartItemWithProduct>>
    
    @Query("SELECT * FROM cart_items WHERE userId = :userId AND productId = :productId")
    suspend fun getCartItem(userId: Int, productId: Int): CartItem?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem): Long
    
    @Update
    suspend fun updateCartItem(cartItem: CartItem)
    
    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)
    
    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: Int)
    
    @Query("SELECT COUNT(*) FROM cart_items WHERE userId = :userId")
    fun getCartItemCount(userId: Int): Flow<Int>
    
    @Query("SELECT SUM(quantity) FROM cart_items WHERE userId = :userId")
    fun getTotalQuantity(userId: Int): Flow<Int?>
}