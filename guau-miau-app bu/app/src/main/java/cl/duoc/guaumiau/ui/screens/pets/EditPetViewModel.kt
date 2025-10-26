package cl.duoc.guaumiau.ui.screens.pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.guaumiau.data.model.Pet
import cl.duoc.guaumiau.data.model.PetFormState
import cl.duoc.guaumiau.data.model.PetType
import cl.duoc.guaumiau.data.repository.PetRepository
import cl.duoc.guaumiau.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditPetViewModel : ViewModel() {
    private val repository = PetRepository()
    
    private val _formState = MutableStateFlow(PetFormState())
    val formState: StateFlow<PetFormState> = _formState.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _updateResult = MutableStateFlow<Boolean?>(null)
    val updateResult: StateFlow<Boolean?> = _updateResult.asStateFlow()
    
    private var currentPetId: Int = 0
    private var currentUserId: Int = 0
    
    fun loadPet(petId: Int) {
        currentPetId = petId
        viewModelScope.launch {
            try {
                val pet = repository.getPetById(petId)
                pet?.let {
                    currentUserId = it.userId
                    _formState.value = PetFormState(
                        name = it.name,
                        type = it.type,
                        photoUri = it.photoUri,
                        isValid = true
                    )
                }
            } catch (e: Exception) {
                _updateResult.value = false
            }
        }
    }
    
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
    
    fun updatePet() {
        if (!_formState.value.isValid) return
        
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val updatedPet = Pet(
                    id = currentPetId,
                    userId = currentUserId,
                    name = _formState.value.name.trim(),
                    type = _formState.value.type!!,
                    photoUri = _formState.value.photoUri
                )
                
                val result = repository.updatePet(updatedPet)
                _updateResult.value = result.isSuccess
            } catch (e: Exception) {
                _updateResult.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deletePet() {
        viewModelScope.launch {
            try {
                val result = repository.deletePet(currentPetId)
                _updateResult.value = result.isSuccess
            } catch (e: Exception) {
                _updateResult.value = false
            }
        }
    }
}