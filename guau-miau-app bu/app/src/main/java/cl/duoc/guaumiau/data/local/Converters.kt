package cl.duoc.guaumiau.data.local

import androidx.room.TypeConverter
import cl.duoc.guaumiau.data.model.PetType
import cl.duoc.guaumiau.data.model.ProductCategory

/**
 * Convertidores de tipos para Room Database
 */
class Converters {
    
    @TypeConverter
    fun fromPetType(value: PetType): String {
        return value.name
    }
    
    @TypeConverter
    fun toPetType(value: String): PetType {
        return PetType.valueOf(value)
    }
    
    @TypeConverter
    fun fromProductCategory(value: ProductCategory): String {
        return value.name
    }
    
    @TypeConverter
    fun toProductCategory(value: String): ProductCategory {
        return ProductCategory.valueOf(value)
    }
}
