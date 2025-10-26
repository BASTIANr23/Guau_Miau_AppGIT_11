package cl.duoc.guaumiau.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cl.duoc.guaumiau.data.model.CartItem
import cl.duoc.guaumiau.data.model.Pet
import cl.duoc.guaumiau.data.model.Product
import cl.duoc.guaumiau.data.model.User

/**
 * IL2.3: Base de datos local con Room
 * Almacenamiento persistente de usuarios, mascotas, productos y carrito
 */
@Database(
    entities = [User::class, Pet::class, Product::class, CartItem::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun petDao(): PetDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "guau_miau_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
