package cl.duoc.guaumiau.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.guaumiau.data.model.RegisterFormState
import cl.duoc.guaumiau.data.model.User
import cl.duoc.guaumiau.data.repository.UserRepository
import cl.duoc.guaumiau.utils.PreferencesManager
import cl.duoc.guaumiau.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * IL2.2: ViewModel para Registro
 * Implementa validaciones complejas y gestión de estado
 */
class RegisterViewModel : ViewModel() {
    
    private val userRepository = UserRepository()
    private val preferencesManager = PreferencesManager()
    
    private val _formState = MutableStateFlow(RegisterFormState())
    val formState: StateFlow<RegisterFormState> = _formState.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _registerResult = MutableStateFlow<Result<Unit>?>(null)
    val registerResult: StateFlow<Result<Unit>?> = _registerResult.asStateFlow()
    
    fun onFullNameChange(name: String) {
        val error = ValidationUtils.validateFullName(name)
        updateFormState { copy(fullName = name, fullNameError = error) }
    }
    
    fun onEmailChange(email: String) {
        val error = ValidationUtils.validateEmail(email)
        updateFormState { copy(email = email, emailError = error) }
    }
    
    fun onPasswordChange(password: String) {
        val error = ValidationUtils.validatePassword(password)
        val matchError = if (_formState.value.confirmPassword.isNotEmpty()) {
            ValidationUtils.validatePasswordMatch(password, _formState.value.confirmPassword)
        } else null
        
        updateFormState { 
            copy(
                password = password, 
                passwordError = error,
                confirmPasswordError = matchError
            ) 
        }
    }
    
    fun onConfirmPasswordChange(confirmPassword: String) {
        val error = ValidationUtils.validatePasswordMatch(_formState.value.password, confirmPassword)
        updateFormState { copy(confirmPassword = confirmPassword, confirmPasswordError = error) }
    }
    
    private fun updateFormState(update: RegisterFormState.() -> RegisterFormState) {
        val newState = _formState.value.update()
        _formState.value = newState.copy(
            isValid = newState.fullNameError == null &&
                    newState.emailError == null &&
                    newState.passwordError == null &&
                    newState.confirmPasswordError == null &&
                    newState.fullName.isNotBlank() &&
                    newState.email.isNotBlank() &&
                    newState.password.isNotBlank() &&
                    newState.confirmPassword.isNotBlank()
        )
    }
    
    fun register() {
        viewModelScope.launch {
            _isLoading.value = true
            _registerResult.value = null
            
            val user = User(
                fullName = _formState.value.fullName,
                email = _formState.value.email,
                password = _formState.value.password
            )
            
            val result = userRepository.registerUser(user)
            
            result.onSuccess { userId ->
                // Guardar sesión
                preferencesManager.saveUserSession(
                    userId = userId.toInt(),
                    email = user.email,
                    name = user.fullName
                )
                _registerResult.value = Result.success(Unit)
            }.onFailure { error ->
                _registerResult.value = Result.failure(error)
            }
            
            _isLoading.value = false
        }
    }
    
    fun clearRegisterResult() {
        _registerResult.value = null
    }
}
