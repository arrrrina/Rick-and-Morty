package com.example.project1.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006A6B),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF80F0F1),
    onPrimaryContainer = Color(0xFF002021),

    secondary = Color(0xFF4A6267),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCDE7ED),
    onSecondaryContainer = Color(0xFF051F23),

    tertiary = Color(0xFF5C5B7E),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE2DFFF),
    onTertiaryContainer = Color(0xFF191837),

    background = Color(0xFFFAFDFD),
    onBackground = Color(0xFF191C1C),

    surface = Color(0xFFFAFDFD),
    onSurface = Color(0xFF191C1C),

    surfaceVariant = Color(0xFFDAE4E5),
    onSurfaceVariant = Color(0xFF3F4849)
)

@Composable
fun Project1Theme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}