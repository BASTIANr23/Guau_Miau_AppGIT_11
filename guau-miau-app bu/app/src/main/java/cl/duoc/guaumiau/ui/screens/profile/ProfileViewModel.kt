package cl.duoc.guaumiau.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.guaumiau.data.model.Pet
import cl.duoc.guaumiau.data.model.User
import cl.duoc.guaumiau.data.repository.PetRepository
import cl.duoc.guaumiau.data.repository.UserRepository
import cl.duoc.guaumiau.utils.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val userRepository = UserRepository()
    private val petRepository = PetRepository()
    private val preferencesManager = PreferencesManager()
    
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()
    
    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets.asStateFlow()
    
    init {
        loadUserData()
    }
    
    private fun loadUserData() {
        viewModelScope.launch {
            val userId = preferencesManager.userId.first()
            if (userId != null) {
                userRepository.getUserById(userId).collect { userData ->
                    _user.value = userData
                }
                
                petRepository.getPetsByUserId(userId).collect { petsData ->
                    _pets.value = petsData
                }
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            preferencesManager.clearUserSession()
        }
    }
}
