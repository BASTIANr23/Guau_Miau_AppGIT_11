package cl.duoc.guaumiau.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.guaumiau.data.model.CartItem
import cl.duoc.guaumiau.data.model.CartItemWithProduct
import cl.duoc.guaumiau.data.model.CartResult
import cl.duoc.guaumiau.data.model.CartState
import cl.duoc.guaumiau.data.repository.CartRepository
import cl.duoc.guaumiau.utils.PreferencesManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val cartRepository = CartRepository()
    private val preferencesManager = PreferencesManager()
    
    private val _cartState = MutableStateFlow(CartState())
    val cartState: StateFlow<CartState> = _cartState.asStateFlow()
    
    private val _operationResult = MutableStateFlow<CartResult?>(null)
    val operationResult: StateFlow<CartResult?> = _operationResult.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadCartItems()
    }
    
    private fun loadCartItems() {
        viewModelScope.launch {
            preferencesManager.userId.collect { userId ->
                if (userId != null) {
                    cartRepository.getCartItems(userId).collect { items ->
                        _cartState.value = CartState.fromItems(items).copy(
                            isLoading = _isLoading.value
                        )
                    }
                }
            }
        }
    }
    
    fun addToCart(productId: Int, quantity: Int = 1) {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = preferencesManager.userId.first() ?: return@launch
            
            val result = cartRepository.addToCart(userId, productId, quantity)
            _operationResult.value = result
            _isLoading.value = false
        }
    }
    
    fun updateQuantity(cartItem: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = cartRepository.updateQuantity(cartItem, newQuantity)
            _operationResult.value = result
            _isLoading.value = false
        }
    }
    
    fun removeFromCart(cartItem: CartItem) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = cartRepository.removeFromCart(cartItem)
            _operationResult.value = result
            _isLoading.value = false
        }
    }
    
    fun clearCart() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = preferencesManager.userId.first() ?: return@launch
            val result = cartRepository.clearCart(userId)
            _operationResult.value = result
            _isLoading.value = false
        }
    }
    
    fun clearOperationResult() {
        _operationResult.value = null
    }
    
    // Función para obtener el número de items en el carrito (para badge)
    fun getCartItemCount(): Flow<Int> {
        return preferencesManager.userId.flatMapLatest { userId ->
            if (userId != null) {
                cartRepository.getCartItemCount(userId)
            } else {
                flowOf(0)
            }
        }
    }
}