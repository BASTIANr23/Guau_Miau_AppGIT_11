package cl.duoc.guaumiau.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Paleta de colores inspirada en mascotas (cálidos y amigables)
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFF8A3D), // Naranja cálido
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDCC8),
    onPrimaryContainer = Color(0xFF2D1600),
    
    secondary = Color(0xFF6B5E52), // Marrón tierra
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF4E4D4),
    onSecondaryContainer = Color(0xFF241913),
    
    tertiary = Color(0xFF4CAF50), // Verde natural
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD7F5D9),
    onTertiaryContainer = Color(0xFF0A2F0C),
    
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    
    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF201B16),
    
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF201B16),
    surfaceVariant = Color(0xFFF0E0CF),
    onSurfaceVariant = Color(0xFF50453A),
    
    outline = Color(0xFF827568),
    outlineVariant = Color(0xFFD3C4B4)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB68E),
    onPrimary = Color(0xFF4F2500),
    primaryContainer = Color(0xFF703800),
    onPrimaryContainer = Color(0xFFFFDCC8),
    
    secondary = Color(0xFFD7C8B8),
    onSecondary = Color(0xFF3A2E25),
    secondaryContainer = Color(0xFF52443A),
    onSecondaryContainer = Color(0xFFF4E4D4),
    
    tertiary = Color(0xFF81C784),
    onTertiary = Color(0xFF1B4D1E),
    tertiaryContainer = Color(0xFF2E6B31),
    onTertiaryContainer = Color(0xFFD7F5D9),
    
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    
    background = Color(0xFF201B16),
    onBackground = Color(0xFFECE0D9),
    
    surface = Color(0xFF201B16),
    onSurface = Color(0xFFECE0D9),
    surfaceVariant = Color(0xFF50453A),
    onSurfaceVariant = Color(0xFFD3C4B4),
    
    outline = Color(0xFF9C8F80),
    outlineVariant = Color(0xFF50453A)
)

@Composable
fun GuauMiauTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
