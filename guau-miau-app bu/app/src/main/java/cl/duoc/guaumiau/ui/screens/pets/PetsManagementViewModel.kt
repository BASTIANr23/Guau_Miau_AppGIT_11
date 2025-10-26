package cl.duoc.guaumiau.ui.screens.pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.guaumiau.data.model.Pet
import cl.duoc.guaumiau.data.repository.PetRepository
import cl.duoc.guaumiau.utils.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PetsManagementViewModel : ViewModel() {
    private val repository = PetRepository()
    private val preferencesManager = PreferencesManager()
    
    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadPets()
    }
    
    fun loadPets() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = preferencesManager.userId.first()
                if (userId != null) {
                    // Usar collect en un job separado para mantener la observación activa
                    repository.getPetsByUserId(userId).collect { petsList ->
                        _pets.value = petsList
                        if (_isLoading.value) {
                            _isLoading.value = false
                        }
                    }
                } else {
                    _pets.value = emptyList()
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                // Manejar error
                _pets.value = emptyList()
                _isLoading.value = false
            }
        }
    }
    
    fun deletePet(petId: Int) {
        viewModelScope.launch {
            try {
                val result = repository.deletePet(petId)
                if (!result.isSuccess) {
                    // Manejar error si es necesario
                }
                // La lista se actualizará automáticamente a través del Flow
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
    
    fun refreshPets() {
        loadPets()
    }
}