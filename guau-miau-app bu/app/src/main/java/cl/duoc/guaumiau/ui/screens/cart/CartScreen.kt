package cl.duoc.guaumiau.ui.screens.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.duoc.guaumiau.data.model.CartItemWithProduct
import cl.duoc.guaumiau.data.model.CartResult
import cl.duoc.guaumiau.ui.components.AnimatedButton
import cl.duoc.guaumiau.ui.components.AnimatedCard
import coil.compose.AsyncImage

/**
 * IL2.1: Pantalla del carrito de compras con interfaz organizada
 * IL2.2: Interacciones con retroalimentación visual
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCheckout: () -> Unit
) {
    val cartState by viewModel.cartState.collectAsState()
    val operationResult by viewModel.operationResult.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var showClearDialog by remember { mutableStateOf(false) }
    
    // Manejar resultados de operaciones
    LaunchedEffect(operationResult) {
        when (operationResult) {
            is CartResult.Success -> {
                // Operación exitosa
                viewModel.clearOperationResult()
            }
            is CartResult.Error -> {
                // Mostrar error (podrías usar un Snackbar aquí)
                viewModel.clearOperationResult()
            }
            null -> { /* No hacer nada */ }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Mi Carrito (${cartState.totalItems})",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    if (cartState.items.isNotEmpty()) {
                        IconButton(onClick = { showClearDialog = true }) {
                            Icon(Icons.Default.Delete, "Limpiar carrito")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            if (cartState.items.isNotEmpty()) {
                CartBottomBar(
                    totalPrice = cartState.totalPrice,
                    onCheckout = onNavigateToCheckout,
                    isLoading = isLoading
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (cartState.items.isEmpty()) {
                EmptyCartContent()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartState.items) { cartItemWithProduct ->
                        AnimatedCard(
                            delayMillis = cartState.items.indexOf(cartItemWithProduct) * 100
                        ) {
                            CartItemCard(
                                cartItemWithProduct = cartItemWithProduct,
                                onQuantityChange = { newQuantity ->
                                    viewModel.updateQuantity(cartItemWithProduct.cartItem, newQuantity)
                                },
                                onRemove = {
                                    viewModel.removeFromCart(cartItemWithProduct.cartItem)
                                },
                                isLoading = isLoading
                            )
                        }
                    }
                    
                    // Espacio extra para el bottom bar
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
            
            // Indicador de carga
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
    
    // Diálogo de confirmación para limpiar carrito
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Limpiar Carrito") },
            text = { Text("¿Estás seguro que deseas eliminar todos los productos del carrito?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearCart()
                        showClearDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Limpiar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun CartItemCard(
    cartItemWithProduct: CartItemWithProduct,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit,
    isLoading: Boolean
) {
    val product = cartItemWithProduct.product
    val cartItem = cartItemWithProduct.cartItem
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Imagen del producto
        AsyncImage(
            model = product.imageUrl,
            contentDescription = product.name,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        
        // Información del producto
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "Para ${product.petType.displayName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Precio
                Text(
                    text = "$${cartItemWithProduct.totalPrice.toInt()}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                // Controles de cantidad
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { onQuantityChange(cartItem.quantity - 1) },
                        enabled = !isLoading && cartItem.quantity > 1
                    ) {
                        Icon(Icons.Default.Remove, "Disminuir")
                    }
                    
                    Text(
                        text = cartItem.quantity.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    
                    IconButton(
                        onClick = { onQuantityChange(cartItem.quantity + 1) },
                        enabled = !isLoading
                    ) {
                        Icon(Icons.Default.Add, "Aumentar")
                    }
                    
                    IconButton(
                        onClick = onRemove,
                        enabled = !isLoading
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartBottomBar(
    totalPrice: Double,
    onCheckout: () -> Unit,
    isLoading: Boolean
) {
    Surface(
        shadowElevation = 8.dp,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Total",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    "$${totalPrice.toInt()}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            AnimatedButton(
                onClick = onCheckout,
                enabled = !isLoading,
                modifier = Modifier.height(56.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(Icons.Default.ShoppingCart, "Proceder")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Proceder al Pago")
            }
        }
    }
}

@Composable
fun EmptyCartContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Text(
                "Tu carrito está vacío",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Agrega productos desde el catálogo",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}