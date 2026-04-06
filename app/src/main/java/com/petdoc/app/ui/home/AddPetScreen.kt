package com.petdoc.app.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.petdoc.app.domain.model.EcuadorData

@Composable
fun AddPetRoute(onNavigateBack: () -> Unit, viewModel: AddPetViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState) { if (uiState is AddPetUiState.Saved) onNavigateBack() }
    AddPetScreen(uiState, viewModel::savePet, onNavigateBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    uiState: AddPetUiState,
    onSave: (String, String, String, String, String, String, String, String, String, String, String, String, String, Long, Long, Long, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("Perro") }
    var raza by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var microchip by remember { mutableStateOf("") }
    var nombreDueno by remember { mutableStateOf("") }
    var cedula by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var provincia by remember { mutableStateOf("Los Ríos") }
    var canton by remember { mutableStateOf("Vinces") }
    var notas by remember { mutableStateOf("") }

    var especieExpanded by remember { mutableStateOf(false) }
    var provinciaExpanded by remember { mutableStateOf(false) }
    var cantonExpanded by remember { mutableStateOf(false) }

    val cantones = EcuadorData.CANTONES_POR_PROVINCIA[provincia] ?: listOf(canton)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Mascota", fontWeight = FontWeight.Bold) },
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
        Column(
            Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (uiState is AddPetUiState.Error) {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Text(uiState.message, Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }

            SectionTitle("🐾 Datos de la Mascota")

            // Especie
            DropdownField("Especie", especie, especieExpanded,
                listOf("Perro", "Gato", "Ave", "Conejo", "Reptil", "Otro"),
                onExpand = { especieExpanded = it },
                onSelect = { especie = it; especieExpanded = false })

            PetField("Nombre *", nombre) { nombre = it }
            PetField("Raza", raza) { raza = it }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PetField("Peso (kg)", peso, Modifier.weight(1f), KeyboardType.Decimal) { peso = it }
                PetField("Color", color, Modifier.weight(1f)) { color = it }
            }

            PetField("Fecha nacimiento (DD/MM/AAAA)", fechaNacimiento) { fechaNacimiento = it }
            PetField("Código Microchip", microchip) { microchip = it }

            HorizontalDivider()
            SectionTitle("💉 Vacunas Agrocalidad Ecuador")

            Card(colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Registra las fechas de vencimiento de las vacunas.",
                        style = MaterialTheme.typography.bodySmall)
                    Text("⚠️ Rabia es obligatoria anual según Agrocalidad.",
                        style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary)
                }
            }

            // Fechas de vacuna (timestamp en ms) - se ingresan como texto DD/MM/AAAA
            var fechaRabiaText by remember { mutableStateOf("") }
            var fechaSextupleText by remember { mutableStateOf("") }
            var fechaFelText by remember { mutableStateOf("") }

            PetField("Venc. Vacuna Rabia (DD/MM/AAAA) *", fechaRabiaText) { fechaRabiaText = it }
            if (especie == "Perro") PetField("Venc. Sextuple (DD/MM/AAAA)", fechaSextupleText) { fechaSextupleText = it }
            if (especie == "Gato") PetField("Venc. Triple Felina (DD/MM/AAAA)", fechaFelText) { fechaFelText = it }

            HorizontalDivider()
            SectionTitle("👤 Datos del Dueño")

            PetField("Nombre del dueño *", nombreDueno) { nombreDueno = it }
            PetField("Cédula ecuatoriana (10 dígitos)", cedula,
                keyboardType = KeyboardType.Number) { if (it.length <= 10) cedula = it }
            PetField("Teléfono (09XXXXXXXX)", telefono,
                keyboardType = KeyboardType.Phone) { telefono = it }
            PetField("Correo electrónico", email,
                keyboardType = KeyboardType.Email) { email = it }

            HorizontalDivider()
            SectionTitle("📍 Ubicación Ecuador")

            // Provincia
            DropdownField("Provincia", provincia, provinciaExpanded,
                EcuadorData.PROVINCIAS,
                onExpand = { provinciaExpanded = it },
                onSelect = { provincia = it; canton = ""; provinciaExpanded = false })

            // Cantón
            DropdownField("Cantón", canton, cantonExpanded,
                cantones,
                onExpand = { cantonExpanded = it },
                onSelect = { canton = it; cantonExpanded = false })

            PetField("Notas adicionales", notas, minLines = 3) { notas = it }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    fun parseDate(s: String): Long {
                        return try {
                            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale("es","EC"))
                            sdf.parse(s)?.time ?: 0L
                        } catch (e: Exception) { 0L }
                    }
                    onSave(nombre, especie, raza, peso, color, fechaNacimiento, microchip,
                        nombreDueno, cedula, telefono, email, provincia, canton,
                        parseDate(fechaRabiaText), parseDate(fechaSextupleText), parseDate(fechaFelText),
                        notas)
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = uiState !is AddPetUiState.Saving
            ) {
                if (uiState is AddPetUiState.Saving) {
                    CircularProgressIndicator(Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("💾 Guardar Mascota", style = MaterialTheme.typography.titleMedium)
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
}

@Composable
private fun PetField(
    label: String, value: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = { Text(label) }, modifier = modifier,
        minLines = minLines,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    label: String, value: String, expanded: Boolean,
    options: List<String>, onExpand: (Boolean) -> Unit, onSelect: (String) -> Unit
) {
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpand) {
        OutlinedTextField(
            value = value, onValueChange = {}, readOnly = true, label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpand(false) }) {
            options.forEach { opt ->
                DropdownMenuItem(text = { Text(opt) }, onClick = { onSelect(opt) })
            }
        }
    }
}
