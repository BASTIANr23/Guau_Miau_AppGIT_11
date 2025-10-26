package cl.duoc.guaumiau.data.repository

import cl.duoc.guaumiau.GuauMiauApplication
import cl.duoc.guaumiau.data.local.PetDao
import cl.duoc.guaumiau.data.model.Pet
import kotlinx.coroutines.flow.Flow

/**
 * IL2.3: Repositorio para gestión de mascotas
 */
class PetRepository {
    
    private val petDao: PetDao = GuauMiauApplication.instance.database.petDao()
    
    suspend fun addPet(pet: Pet): Result<Long> {
        return try {
            val petId = petDao.insertPet(pet)
            Result.success(petId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getPetsByUserId(userId: Int): Flow<List<Pet>> {
        return petDao.getPetsByUserId(userId)
    }
    
    suspend fun getPetById(petId: Int): Pet? {
        return petDao.getPetById(petId)
    }
    
    suspend fun updatePet(pet: Pet): Result<Unit> {
        return try {
            petDao.insertPet(pet) // Room usa INSERT OR REPLACE
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deletePet(petId: Int): Result<Unit> {
        return try {
            petDao.deletePet(petId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
