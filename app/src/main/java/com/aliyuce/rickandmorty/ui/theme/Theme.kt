package com.aliyuce.rickandmorty.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
    darkColorScheme(
        primary = Primary,
        onPrimary = OnPrimary,
        secondary = Secondary,
        onSecondary = OnSecondary,
        background = BackgroundDark,
        surface = SurfaceDark,
        onBackground = OnBackgroundDark,
        onSurface = OnBackgroundDark,
        error = Error,
        onError = OnError,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Primary,
        onPrimary = OnPrimary,
        secondary = Secondary,
        onSecondary = OnSecondary,
        background = BackgroundLight,
        surface = SurfaceLight,
        onBackground = OnBackgroundLight,
        onSurface = OnBackgroundLight,
        error = Error,
        onError = OnError,
    )

@Composable
fun RickAndMortyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
