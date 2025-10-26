package cl.duoc.guaumiau.data.repository

import cl.duoc.guaumiau.GuauMiauApplication
import cl.duoc.guaumiau.data.local.UserDao
import cl.duoc.guaumiau.data.model.User
import kotlinx.coroutines.flow.Flow

/**
 * IL2.3: Repositorio para gestión de usuarios
 * Patrón Repository para separar lógica de datos
 */
class UserRepository {
    
    private val userDao: UserDao = GuauMiauApplication.instance.database.userDao()
    
    suspend fun registerUser(user: User): Result<Long> {
        return try {
            // Verificar si el email ya existe
            if (userDao.emailExists(user.email)) {
                Result.failure(Exception("El correo electrónico ya está registrado"))
            } else {
                val userId = userDao.insertUser(user)
                Result.success(userId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val user = userDao.login(email, password)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Credenciales incorrectas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getUserById(userId: Int): Flow<User?> {
        return userDao.getUserById(userId)
    }
    
    suspend fun emailExists(email: String): Boolean {
        return userDao.emailExists(email)
    }
}
