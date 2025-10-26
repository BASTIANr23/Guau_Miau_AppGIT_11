package cl.duoc.guaumiau.ui.screens.pets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.guaumiau.data.model.Pet
import cl.duoc.guaumiau.data.model.PetType
import cl.duoc.guaumiau.ui.components.AnimatedButton
import cl.duoc.guaumiau.ui.components.AnimatedCard
import coil.compose.AsyncImage

/**
 * IL2.1: Pantalla de gestión de mascotas
 * IL2.2: Interacciones con retroalimentación visual
 * IL2.4: Integración con recursos nativos (cámara para fotos)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetsManagementScreen(
    viewModel: PetsManagementViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAddPet: () -> Unit,
    onNavigateToEditPet: (Int) -> Unit
) {
    val pets by viewModel.pets.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<Pet?>(null) }
    
    // Cargar mascotas al iniciar y refrescar cuando se regrese
    LaunchedEffect(Unit) {
        viewModel.loadPets()
    }
    
    // Refrescar cuando cambie el número de mascotas (útil para detectar cambios)
    LaunchedEffect(pets.size) {
        // Este efecto se ejecutará cuando cambie el tamaño de la lista
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Mis Mascotas (${pets.size})",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddPet,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Agregar mascota")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (pets.isEmpty()) {
                EmptyPetsContent(onAddPet = onNavigateToAddPet)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pets) { pet ->
                        AnimatedCard(
                            delayMillis = pets.indexOf(pet) * 100
                        ) {
                            PetCard(
                                pet = pet,
                                onEdit = { onNavigateToEditPet(pet.id) },
                                onDelete = { showDeleteDialog = pet }
                            )
                        }
                    }
                    
                    // Espacio extra para el FAB
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
    
    // Diálogo de confirmación para eliminar
    showDeleteDialog?.let { pet ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Eliminar Mascota") },
            text = { 
                Text("¿Estás seguro que deseas eliminar a ${pet.name}? Esta acción no se puede deshacer.") 
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deletePet(pet.id)
                        showDeleteDialog = null
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun PetCard(
    pet: Pet,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Foto de la mascota
        if (pet.photoUri != null) {
            AsyncImage(
                model = pet.photoUri,
                contentDescription = "Foto de ${pet.name}",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Surface(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        when (pet.type) {
                            PetType.DOG -> Icons.Default.Pets
                            PetType.CAT -> Icons.Default.Pets
                            PetType.BIRD -> Icons.Default.Favorite
                            PetType.OTHER -> Icons.Default.Star
                        },
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
        
        // Información de la mascota
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = pet.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = pet.type.displayName,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
        
        // Botones de acción
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onEdit,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Icon(
                    Icons.Default.Edit,
                    "Editar",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            IconButton(
                onClick = onDelete,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Icon(
                    Icons.Default.Delete,
                    "Eliminar",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

@Composable
fun EmptyPetsContent(onAddPet: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                Icons.Default.Pets,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Text(
                "No tienes mascotas registradas",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Agrega tu primera mascota para personalizar tu experiencia",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            AnimatedButton(onClick = onAddPet) {
                Icon(Icons.Default.Add, "Agregar")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Agregar Primera Mascota")
            }
        }
    }
}