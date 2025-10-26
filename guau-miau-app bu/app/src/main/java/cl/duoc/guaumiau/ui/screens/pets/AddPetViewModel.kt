package cl.duoc.guaumiau.ui.screens.pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.guaumiau.data.model.Pet
import cl.duoc.guaumiau.data.model.PetFormState
import cl.duoc.guaumiau.data.model.PetType
import cl.duoc.guaumiau.data.repository.PetRepository
import cl.duoc.guaumiau.utils.PreferencesManager
import cl.duoc.guaumiau.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddPetViewModel : ViewModel() {
    private val repository = PetRepository()
    private val preferencesManager = PreferencesManager()
    
    private val _formState = MutableStateFlow(PetFormState())
    val formState: StateFlow<PetFormState> = _formState.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _addResult = MutableStateFlow<Boolean?>(null)
    val addResult: StateFlow<Boolean?> = _addResult.asStateFlow()
    
    fun updateName(name: String) {
        val nameError = ValidationUtils.validatePetName(name)
        _formState.value = _formState.value.copy(
            name = name,
            nameError = nameError,
            isValid = validateForm(name, _formState.value.type)
        )
    }
    
    fun updateType(type: PetType) {
        _formState.value = _formState.value.copy(
            type = type,
            typeError = null,
            isValid = validateForm(_formState.value.name, type)
        )
    }
    
    fun updatePhoto(photoUri: String) {
        _formState.value = _formState.value.copy(
            photoUri = photoUri
        )
    }
    
    private fun validateForm(name: String, type: PetType?): Boolean {
        val nameError = ValidationUtils.validatePetName(name)
        val typeError = if (type == null) "Debes seleccionar un tipo de mascota" else null
        
        return nameError == null && typeError == null
    }
    
    fun addPet() {
        if (!_formState.value.isValid) return
        
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val userId = preferencesManager.userId.first() ?: return@launch
                
                val newPet = Pet(
                    id = 0,
                    userId = userId,
                    name = _formState.value.name.trim(),
                    type = _formState.value.type!!,
                    photoUri = _formState.value.photoUri
                )
                
                val result = repository.addPet(newPet)
                _addResult.value = result.isSuccess
            } catch (e: Exception) {
                _addResult.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}