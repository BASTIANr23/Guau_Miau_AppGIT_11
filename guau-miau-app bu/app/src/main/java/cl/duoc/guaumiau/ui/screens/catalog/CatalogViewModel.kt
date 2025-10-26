package cl.duoc.guaumiau.ui.screens.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.guaumiau.data.model.PetType
import cl.duoc.guaumiau.data.model.Product
import cl.duoc.guaumiau.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CatalogViewModel : ViewModel() {
    private val repository = ProductRepository()
    
    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())
    
    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()
    
    private val _selectedFilter = MutableStateFlow<PetType?>(null)
    val selectedFilter: StateFlow<PetType?> = _selectedFilter.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    val filteredProducts: StateFlow<List<Product>> = combine(
        _allProducts,
        _selectedFilter,
        _searchQuery
    ) { products, filter, query ->
        products
            .filter { product ->
                (filter == null || product.petType == filter) &&
                (query.isEmpty() || product.name.contains(query, ignoreCase = true) ||
                 product.description.contains(query, ignoreCase = true))
            }
    }.stateFlow(viewModelScope, emptyList())
    
    init {
        loadProducts()
    }
    
    private fun loadProducts() {
        viewModelScope.launch {
            // Inicializar productos en la base de datos
            repository.initializeProducts()
            _allProducts.value = repository.getAllProducts()
        }
    }
    
    fun filterByPetType(petType: PetType?) {
        _selectedFilter.value = petType
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            val product = repository.getProductById(productId)
            _selectedProduct.value = product
        }
    }
    
    fun getProductById(productId: Int): Product? {
        return _allProducts.value.find { it.id == productId }
    }
}

private fun <T> kotlinx.coroutines.flow.Flow<T>.stateFlow(
    scope: kotlinx.coroutines.CoroutineScope,
    initialValue: T
): StateFlow<T> {
    val stateFlow = MutableStateFlow(initialValue)
    scope.launch {
        collect { stateFlow.value = it }
    }
    return stateFlow.asStateFlow()
}
