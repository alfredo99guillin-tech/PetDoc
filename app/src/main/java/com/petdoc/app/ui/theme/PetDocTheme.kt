package com.petdoc.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 🇪🇨 Paleta Verde Amazonía Ecuador
private val EcuadorColorScheme = lightColorScheme(
    primary          = Color(0xFF2E7D32),  // Verde Bosque
    onPrimary        = Color.White,
    primaryContainer = Color(0xFFA5D6A7),  // Verde claro
    secondary        = Color(0xFF81C784),  // Verde suave
    onSecondary      = Color(0xFF1B5E20),
    tertiary         = Color(0xFFFF8F00),  // Amarillo ecuatoriano
    background       = Color(0xFFF1F8E9),  // Fondo verde muy claro
    surface          = Color.White,
    error            = Color(0xFFD32F2F),
    onBackground     = Color(0xFF1B5E20),
    onSurface        = Color(0xFF212121),
    onSurfaceVariant = Color(0xFF616161)
)

@Composable
fun PetDocTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EcuadorColorScheme,
        content = content
    )
}
