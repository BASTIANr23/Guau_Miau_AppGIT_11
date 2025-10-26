package cl.duoc.guaumiau.data.local

import androidx.room.*
import cl.duoc.guaumiau.data.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * IL2.3: DAO para operaciones de productos
 */
@Dao
interface ProductDao {
    
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Product>>
    
    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: Int): Product?
    
    @Query("SELECT * FROM products WHERE petType = :petType")
    fun getProductsByPetType(petType: String): Flow<List<Product>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)
    
    @Update
    suspend fun updateProduct(product: Product)
    
    @Delete
    suspend fun deleteProduct(product: Product)
    
    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
}