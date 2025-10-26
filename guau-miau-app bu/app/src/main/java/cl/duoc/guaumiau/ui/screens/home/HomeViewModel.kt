package cl.duoc.guaumiau.ui.screens.home

import androidx.lifecycle.ViewModel
import cl.duoc.guaumiau.utils.PreferencesManager
import kotlinx.coroutines.flow.Flow

/**
 * IL2.2: ViewModel para la pantalla de inicio
 * Gestión de estado de sesión
 */
class HomeViewModel : ViewModel() {
    
    private val preferencesManager = PreferencesManager()
    
    val isLoggedIn: Flow<Boolean> = preferencesManager.isLoggedIn
}
