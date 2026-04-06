package com.petdoc.app.ui.map

import android.Manifest
import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.petdoc.app.domain.model.EcuadorData

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🗺️ Veterinarias Ecuador", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
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
            // ✅ Botón SOS — ruta a veterinaria más cercana
            FloatingActionButton(
                onClick = {
                    // Abre Google Maps buscando veterinarias 24h cercanas
                    val uri = Uri.parse("geo:0,0?q=veterinaria+24+horas+cerca")
                    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                        setPackage("com.google.android.apps.maps")
                    }
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    } else {
                        // Fallback al navegador
                        context.startActivity(Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://maps.google.com/?q=veterinaria+24+horas")))
                    }
                },
                containerColor = Color(0xFFD32F2F)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Emergency, contentDescription = "SOS",
                        tint = Color.White, modifier = Modifier.size(20.dp))
                    Text("SOS", color = Color.White, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            // Banner ubicación
            if (!locationPermission.status.isGranted) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Activa tu ubicación", fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium)
                            Text("Para encontrar la veterinaria más cercana",
                                style = MaterialTheme.typography.bodySmall)
                        }
                        TextButton(onClick = { locationPermission.launchPermissionRequest() }) {
                            Text("Activar")
                        }
                    }
                }
            }

            // Lista de emergencias Ecuador
            Text(
                "📞 Contactos de Emergencia Animal — Ecuador",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(EcuadorData.NUMEROS_EMERGENCIA) { contacto ->
                    EmergenciaCard(contacto = contacto, context = context)
                }

                item {
                    Spacer(Modifier.height(80.dp)) // espacio para el FAB
                }
            }
        }
    }
}

@Composable
private fun EmergenciaCard(
    contacto: EcuadorData.EmergenciaContacto,
    context: android.content.Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocalHospital,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
            Column(modifier = Modifier.weight(1f).padding(horizontal = 10.dp)) {
                Text(contacto.nombre, fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium)
                Text(contacto.telefono, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary)
                Text("📍 ${contacto.provincia}", style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row {
                // Llamar
                IconButton(onClick = {
                    val limpio = contacto.telefono.replace(" ", "").replace("-", "")
                    context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$limpio")))
                }) {
                    Icon(Icons.Default.Call, contentDescription = "Llamar",
                        tint = MaterialTheme.colorScheme.primary)
                }
                // WhatsApp
                IconButton(onClick = {
                    val numero = contacto.telefono.replace("+", "").replace(" ", "")
                    val msg = Uri.encode("Hola, necesito asistencia para mi mascota registrada en PetDoc.")
                    val intent = Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://wa.me/$numero?text=$msg"))
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Default.Message, contentDescription = "WhatsApp",
                        tint = Color(0xFF25D366))
                }
            }
        }
    }
}
