package cl.duoc.guaumiau.ui.screens.pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.guaumiau.data.model.Pet
import cl.duoc.guaumiau.data.model.PetType
import cl.duoc.guaumiau.data.repository.PetRepository
import cl.duoc.guaumiau.utils.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PetsViewModel : ViewModel() {
    private val repository = PetRepository()
    private val preferencesManager = PreferencesManager()
    
    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets.asStateFlow()
    
    private val _currentPetName = MutableStateFlow("")
    val currentPetName: StateFlow<String> = _currentPetName.asStateFlow()
    
    private val _currentPetType = MutableStateFlow<PetType?>(null)
    val currentPetType: StateFlow<PetType?> = _currentPetType.asStateFlow()
    
    private val _currentPetPhoto = MutableStateFlow<String?>(null)
    val currentPetPhoto: StateFlow<String?> = _currentPetPhoto.asStateFlow()
    
    private val _petNameError = MutableStateFlow<String?>(null)
    val petNameError: StateFlow<String?> = _petNameError.asStateFlow()
    
    private val _petTypeError = MutableStateFlow<String?>(null)
    val petTypeError: StateFlow<String?> = _petTypeError.asStateFlow()
    
    private val _showNotificationPermission = MutableStateFlow(false)
    val showNotificationPermission: StateFlow<Boolean> = _showNotificationPermission.asStateFlow()
    
    fun updateCurrentPetName(name: String) {
        _currentPetName.value = name
        validatePetName()
    }
    
    fun updateCurrentPetType(type: PetType) {
        _currentPetType.value = type
        validatePetType()
    }
    
    fun updateCurrentPetPhoto(photoUri: String) {
        _currentPetPhoto.value = photoUri
    }
    
    private fun validatePetName(): Boolean {
        val name = _currentPetName.value
        _petNameError.value = when {
            name.isBlank() -> "El nombre es obligatorio"
            name.length > 50 -> "El nombre no puede exceder 50 caracteres"
            else -> null
        }
        return _petNameError.value == null
    }
    
    private fun validatePetType(): Boolean {
        _petTypeError.value = if (_currentPetType.value == null) {
            "Debes seleccionar un tipo de mascota"
        } else null
        return _petTypeError.value == null
    }
    
    fun addPet() {
        val isNameValid = validatePetName()
        val isTypeValid = validatePetType()
        
        if (isNameValid && isTypeValid) {
            viewModelScope.launch {
                val userId = preferencesManager.userId.first() ?: 0
                
                val newPet = Pet(
                    id = 0,
                    userId = userId,
                    name = _currentPetName.value.trim(),
                    type = _currentPetType.value!!,
                    photoUri = _currentPetPhoto.value
                )
                
                _pets.value = _pets.value + newPet
                
                _currentPetName.value = ""
                _currentPetType.value = null
                _currentPetPhoto.value = null
                _petNameError.value = null
                _petTypeError.value = null
            }
        }
    }
    
    fun removePet(index: Int) {
        _pets.value = _pets.value.filterIndexed { i, _ -> i != index }
    }
    
    fun savePets(): Boolean {
        if (_pets.value.isEmpty()) return true
        
        viewModelScope.launch {
            _pets.value.forEach { pet ->
                repository.addPet(pet)
            }
        }
        return true
    }
}
