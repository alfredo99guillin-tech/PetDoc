package com.petdoc.app.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.petdoc.app.domain.model.Pet
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeRoute(
    onPetClick: (Int) -> Unit,
    onAddPetClick: () -> Unit,
    onMapClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(uiState, onPetClick, onAddPetClick, onMapClick, viewModel::deletePet)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onPetClick: (Int) -> Unit,
    onAddPetClick: () -> Unit,
    onMapClick: () -> Unit,
    onDelete: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("🐾 PetDoc Ecuador", fontWeight = FontWeight.Bold)
                        Text("Pasaporte Digital • Agrocalidad",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f))
                    }
                },
                actions = {
                    IconButton(onClick = onMapClick) {
                        Icon(Icons.Default.LocationOn, "Mapa veterinarias",
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddPetClick,
                containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Add, "Agregar mascota")
            }
        }
    ) { padding ->
        when (uiState) {
            HomeUiState.Loading -> Box(Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is HomeUiState.Error -> Box(Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center) {
                Text("Error: ${uiState.message}", color = MaterialTheme.colorScheme.error)
            }
            is HomeUiState.Success -> {
                if (uiState.pets.isEmpty()) {
                    EmptyState(Modifier.padding(padding))
                } else {
                    LazyColumn(
                        Modifier.padding(padding).fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.pets, key = { it.id }) { pet ->
                            PetCard(pet, { onPetClick(pet.id) }, { onDelete(pet.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PetCard(pet: Pet, onClick: () -> Unit, onDelete: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    val ahora = System.currentTimeMillis()
    val rabiaVencida = pet.fechaVacunaRabia in 1 until ahora
    val rabiaProxima = pet.fechaVacunaRabia > ahora &&
            pet.fechaVacunaRabia - ahora < 15L * 24 * 60 * 60 * 1000

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Eliminar mascota") },
            text = { Text("¿Eliminar a ${pet.nombreMascota}?") },
            confirmButton = {
                TextButton(onClick = { onDelete(); showDialog = false }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Cancelar") } }
        )
    }

    Card(onClick = onClick, modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Pets, null,
                tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp))
            Column(Modifier.weight(1f).padding(horizontal = 10.dp)) {
                Text(pet.nombreMascota, style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Text("${pet.especie} • ${pet.raza}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary)
                Text("Dueño: ${pet.nombreDueno} | ${pet.canton}, ${pet.provincia}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                // Badge vacuna rabia
                when {
                    rabiaVencida -> VacunaBadge("⚠️ Rabia vencida", Color(0xFFD32F2F))
                    rabiaProxima -> VacunaBadge("🔔 Rabia próxima", Color(0xFFF57F17))
                    pet.fechaVacunaRabia > ahora -> VacunaBadge("✅ Vacunas al día", Color(0xFF2E7D32))
                }
            }
            IconButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun VacunaBadge(text: String, color: Color) {
    Surface(color = color.copy(alpha = 0.12f), shape = MaterialTheme.shapes.small) {
        Text(text, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("🐾", style = MaterialTheme.typography.displayLarge)
            Text("¡Registra tu primera mascota!", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Toca el botón + para comenzar", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
