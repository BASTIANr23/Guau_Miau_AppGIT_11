package cl.duoc.guaumiau.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.guaumiau.data.model.LoginFormState
import cl.duoc.guaumiau.data.repository.UserRepository
import cl.duoc.guaumiau.utils.PreferencesManager
import cl.duoc.guaumiau.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * IL2.2: ViewModel para Login
 * Gestión de estado y lógica de control
 */
class LoginViewModel : ViewModel() {
    
    private val userRepository = UserRepository()
    private val preferencesManager = PreferencesManager()
    
    private val _formState = MutableStateFlow(LoginFormState())
    val formState: StateFlow<LoginFormState> = _formState.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _loginResult = MutableStateFlow<Result<Unit>?>(null)
    val loginResult: StateFlow<Result<Unit>?> = _loginResult.asStateFlow()
    
    fun onEmailChange(email: String) {
        val error = ValidationUtils.validateEmail(email)
        _formState.value = _formState.value.copy(
            email = email,
            emailError = error,
            isValid = error == null && _formState.value.passwordError == null
        )
    }
    
    fun onPasswordChange(password: String) {
        _formState.value = _formState.value.copy(
            password = password,
            passwordError = null,
            isValid = _formState.value.emailError == null && password.isNotBlank()
        )
    }
    
    fun login() {
        viewModelScope.launch {
            _isLoading.value = true
            _loginResult.value = null
            
            val result = userRepository.login(
                email = _formState.value.email,
                password = _formState.value.password
            )
            
            result.onSuccess { user ->
                // Guardar sesión
                preferencesManager.saveUserSession(
                    userId = user.id,
                    email = user.email,
                    name = user.fullName
                )
                _loginResult.value = Result.success(Unit)
            }.onFailure { error ->
                _loginResult.value = Result.failure(error)
            }
            
            _isLoading.value = false
        }
    }
    
    fun clearLoginResult() {
        _loginResult.value = null
    }
}
