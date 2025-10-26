package cl.duoc.guaumiau.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cl.duoc.guaumiau.data.model.Pet
import kotlinx.coroutines.flow.Flow

/**
 * IL2.3: DAO para operaciones de Mascota
 */
@Dao
interface PetDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: Pet): Long
    
    @Query("SELECT * FROM pets WHERE userId = :userId")
    fun getPetsByUserId(userId: Int): Flow<List<Pet>>
    
    @Query("SELECT * FROM pets WHERE id = :petId")
    suspend fun getPetById(petId: Int): Pet?
    
    @Query("DELETE FROM pets WHERE id = :petId")
    suspend fun deletePet(petId: Int)
}
