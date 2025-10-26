package cl.duoc.guaumiau.ui.screens.pets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import cl.duoc.guaumiau.ui.components.AnimatedErrorMessage
import cl.duoc.guaumiau.ui.components.CameraCapture

/**
 * IL2.1: Pantalla de edición de mascota con validaciones
 * IL2.2: Retroalimentación visual mejorada
 * IL2.4: Integración con recursos nativos (cámara)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPetScreen(
    petId: Int,
    viewModel: EditPetViewModel,
    onNavigateBack: () -> Unit,
    onPetUpdated: () -> Unit
) {
    val formState by viewModel.formState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val updateResult by viewModel.updateResult.collectAsState()
    var showTypeDropdown by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(petId) {
        viewModel.loadPet(petId)
    }
    
    LaunchedEffect(updateResult) {
        if (updateResult == true) {
            onPetUpdated()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Mascota") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            Icons.Default.Delete,
                            "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedCard(delayMillis = 100) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Información de ${formState.name.ifEmpty { "tu mascota" }}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        "Actualiza los datos de tu mascota",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Foto de mascota
                    CameraCapture(
                        currentImageUri = formState.photoUri,
                        onImageCaptured = { uri -> viewModel.updatePhoto(uri) }
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Nombre
                    OutlinedTextField(
                        value = formState.name,
                        onValueChange = { viewModel.updateName(it) },
                        label = { Text("Nombre de la Mascota *") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = formState.nameError != null,
                        supportingText = formState.nameError?.let { { Text(it) } },
                        singleLine = true,
                        leadingIcon = {
                            Icon(Icons.Default.Pets, contentDescription = null)
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Tipo de mascota
                    ExposedDropdownMenuBox(
                        expanded = showTypeDropdown,
                        onExpandedChange = { showTypeDropdown = it }
                    ) {
                        OutlinedTextField(
                            value = formState.type?.displayName ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo de Mascota *") },
                            trailingIcon = { 
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = showTypeDropdown) 
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            isError = formState.typeError != null,
                            supportingText = formState.typeError?.let { { Text(it) } },
                            leadingIcon = {
                                Icon(
                                    when (formState.type) {
                                        PetType.DOG -> Icons.Default.Pets
                                        PetType.CAT -> Icons.Default.Pets
                                        PetType.BIRD -> Icons.Default.Favorite
                                        PetType.OTHER -> Icons.Default.Star
                                        null -> Icons.Default.QuestionMark
                                    },
                                    contentDescription = null
                                )
                            }
                        )
                        
                        ExposedDropdownMenu(
                            expanded = showTypeDropdown,
                            onDismissRequest = { showTypeDropdown = false }
                        ) {
                            PetType.entries.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.displayName) },
                                    onClick = {
                                        viewModel.updateType(type)
                                        showTypeDropdown = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            when (type) {
                                                PetType.DOG -> Icons.Default.Pets
                                                PetType.CAT -> Icons.Default.Pets
                                                PetType.BIRD -> Icons.Default.Favorite
                                                PetType.OTHER -> Icons.Default.Star
                                            },
                                            contentDescription = null
                                        )
                                    }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Botón guardar
                    AnimatedButton(
                        onClick = { viewModel.updatePet() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = formState.isValid && !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Icon(Icons.Default.Save, "Guardar")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar Cambios")
                    }
                }
            }
            
            // Mensaje de error general
            AnimatedErrorMessage(
                errorMessage = if (updateResult == false) "Error al actualizar la mascota" else null
            )
        }
    }
    
    // Diálogo de confirmación para eliminar
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Mascota") },
            text = { 
                Text("¿Estás seguro que deseas eliminar a ${formState.name}? Esta acción no se puede deshacer.") 
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deletePet()
                        showDeleteDialog = false
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}