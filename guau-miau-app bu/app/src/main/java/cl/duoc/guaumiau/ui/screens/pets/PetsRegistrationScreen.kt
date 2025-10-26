package cl.duoc.guaumiau.ui.screens.pets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.guaumiau.data.model.PetType
import cl.duoc.guaumiau.ui.components.AnimatedButton
import cl.duoc.guaumiau.ui.components.AnimatedCard
import cl.duoc.guaumiau.ui.components.CameraCapture

/**
 * IL2.1: Formulario estructurado con validación
 * IL2.2: Retroalimentación visual en tiempo real
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetsRegistrationScreen(
    viewModel: PetsViewModel,
    onNavigateToCatalog: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val pets by viewModel.pets.collectAsState()
    val currentPetName by viewModel.currentPetName.collectAsState()
    val currentPetType by viewModel.currentPetType.collectAsState()
    val currentPetPhoto by viewModel.currentPetPhoto.collectAsState()
    val petNameError by viewModel.petNameError.collectAsState()
    val petTypeError by viewModel.petTypeError.collectAsState()
    var showTypeDropdown by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registra tus Mascotas") },
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Cuéntanos sobre tus mascotas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                "Esto nos ayudará a recomendarte los mejores productos",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Formulario de nueva mascota
            AnimatedCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                delayMillis = 200
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Agregar Mascota",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Foto de mascota
                    CameraCapture(
                        currentImageUri = currentPetPhoto,
                        onImageCaptured = { uri -> viewModel.updateCurrentPetPhoto(uri) }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Tipo de mascota
                    ExposedDropdownMenuBox(
                        expanded = showTypeDropdown,
                        onExpandedChange = { showTypeDropdown = it }
                    ) {
                        OutlinedTextField(
                            value = currentPetType?.displayName ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo de Mascota *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showTypeDropdown) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            isError = petTypeError != null,
                            supportingText = petTypeError?.let { { Text(it) } },
                            colors = OutlinedTextFieldDefaults.colors()
                        )
                        
                        ExposedDropdownMenu(
                            expanded = showTypeDropdown,
                            onDismissRequest = { showTypeDropdown = false }
                        ) {
                            PetType.entries.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.displayName) },
                                    onClick = {
                                        viewModel.updateCurrentPetType(type)
                                        showTypeDropdown = false
                                    }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Nombre de mascota
                    OutlinedTextField(
                        value = currentPetName,
                        onValueChange = { viewModel.updateCurrentPetName(it) },
                        label = { Text("Nombre de la Mascota *") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = petNameError != null,
                        supportingText = petNameError?.let { { Text(it) } },
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    AnimatedButton(
                        onClick = { viewModel.addPet() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, "Agregar")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Agregar Mascota")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Lista de mascotas agregadas
            if (pets.isNotEmpty()) {
                Text(
                    "Mis Mascotas (${pets.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(pets) { index, pet ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        when (pet.type) {
                                            PetType.DOG -> Icons.Default.Pets
                                            PetType.CAT -> Icons.Default.Pets
                                            PetType.BIRD -> Icons.Default.Favorite
                                            PetType.OTHER -> Icons.Default.Star
                                        },
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                    Column {
                                        Text(
                                            pet.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            pet.type.displayName,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                
                                IconButton(onClick = { viewModel.removePet(index) }) {
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
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botón continuar
            AnimatedButton(
                onClick = {
                    if (viewModel.savePets()) {
                        onNavigateToCatalog()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = pets.isNotEmpty()
            ) {
                Text("Continuar al Catálogo")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, "Continuar")
            }
            
            if (pets.isEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Puedes omitir este paso, pero te recomendamos agregar al menos una mascota",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
