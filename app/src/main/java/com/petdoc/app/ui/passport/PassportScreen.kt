package com.petdoc.app.ui.passport

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.petdoc.app.domain.model.Pet
import com.petdoc.app.domain.usecase.QrGenerator
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PassportRoute(petId: Int, onNavigateBack: () -> Unit, viewModel: PassportViewModel = hiltViewModel()) {
    LaunchedEffect(petId) { viewModel.loadPet(petId) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PassportScreen(uiState, onNavigateBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassportScreen(uiState: PassportUiState, onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pasaporte Digital", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Atrás")
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
        when (uiState) {
            PassportUiState.Loading -> Box(Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            PassportUiState.NotFound -> Box(Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center) { Text("Mascota no encontrada") }
            is PassportUiState.Success -> PassportContent(uiState.pet, Modifier.padding(padding))
        }
    }
}

@Composable
private fun PassportContent(pet: Pet, modifier: Modifier = Modifier) {
    val qrBitmap = remember(pet) { QrGenerator.generateBitmap(pet) }
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("es", "EC"))
    val ahora = System.currentTimeMillis()
    val rabiaOk = pet.fechaVacunaRabia > ahora

    Column(
        modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header verde Ecuador
        Card(
            Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Column(Modifier.padding(20.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🇪🇨 PASAPORTE DIGITAL", style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                Text("PetDoc Ecuador • Agrocalidad", style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
                Spacer(Modifier.height(8.dp))
                Text(pet.nombreMascota, style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                Text("${pet.especie} • ${pet.raza}", style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f))
                Spacer(Modifier.height(4.dp))
                Surface(
                    color = if (rabiaOk) Color(0xFF1B5E20) else Color(0xFFB71C1C),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        if (rabiaOk) "✅ Vacunas al día" else "⚠️ Rabia VENCIDA — Visitar veterinario",
                        Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = Color.White, style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // QR Card
        Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
            Column(Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Código QR — Pasaporte Digital", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                qrBitmap?.let {
                    Image(it.asImageBitmap(), contentDescription = "QR Pasaporte", Modifier.size(200.dp))
                } ?: CircularProgressIndicator()
                Spacer(Modifier.height(8.dp))
                Text("Escanea para ver ficha completa y estado Agrocalidad",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        // Vacunas
        InfoCard("💉 Vacunas Agrocalidad Ecuador") {
            val fechaRabia = if (pet.fechaVacunaRabia > 0) sdf.format(Date(pet.fechaVacunaRabia)) else "No registrada"
            InfoRow("Rabia (obligatoria)", fechaRabia, if (!rabiaOk && pet.fechaVacunaRabia > 0) Color(0xFFD32F2F) else null)
            if (pet.fechaVacunaSextuple > 0)
                InfoRow("Séxtuple", sdf.format(Date(pet.fechaVacunaSextuple)))
            if (pet.fechaVacunaTripleFelina > 0)
                InfoRow("Triple Felina", sdf.format(Date(pet.fechaVacunaTripleFelina)))
        }

        // Datos mascota
        InfoCard("📋 Datos de la Mascota") {
            InfoRow("Nacimiento", pet.fechaNacimiento)
            InfoRow("Color / Pelaje", pet.color)
            InfoRow("Peso", "${pet.peso} kg")
            InfoRow("Microchip", pet.codigoMicrochip.ifEmpty { "N/A" })
            InfoRow("Ubicación", "${pet.canton}, ${pet.provincia}")
        }

        // Datos dueño
        InfoCard("👤 Datos del Dueño") {
            InfoRow("Nombre", pet.nombreDueno)
            InfoRow("Cédula", pet.cedulaDueno)
            InfoRow("Emergencia", pet.contactoEmergencia)
            InfoRow("Email", pet.emailDueno)
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun InfoCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String, valueColor: Color? = null) {
    if (value.isEmpty()) return
    Row(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text("$label: ", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        Text(value, style = MaterialTheme.typography.bodyMedium,
            color = valueColor ?: MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
