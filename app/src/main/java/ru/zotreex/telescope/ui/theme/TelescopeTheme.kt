package com.example.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme
import androidx.tv.material3.lightColorScheme
import ru.zotreex.telescope.ui.theme.TypographyLocal
import ru.zotreex.telescope.ui.theme.backgroundDark
import ru.zotreex.telescope.ui.theme.backgroundLight
import ru.zotreex.telescope.ui.theme.errorContainerDark
import ru.zotreex.telescope.ui.theme.errorContainerLight
import ru.zotreex.telescope.ui.theme.errorDark
import ru.zotreex.telescope.ui.theme.errorLight
import ru.zotreex.telescope.ui.theme.inverseOnSurfaceDark
import ru.zotreex.telescope.ui.theme.inverseOnSurfaceLight
import ru.zotreex.telescope.ui.theme.inversePrimaryDark
import ru.zotreex.telescope.ui.theme.inversePrimaryLight
import ru.zotreex.telescope.ui.theme.inverseSurfaceDark
import ru.zotreex.telescope.ui.theme.inverseSurfaceLight
import ru.zotreex.telescope.ui.theme.onBackgroundDark
import ru.zotreex.telescope.ui.theme.onBackgroundLight
import ru.zotreex.telescope.ui.theme.onErrorContainerDark
import ru.zotreex.telescope.ui.theme.onErrorContainerLight
import ru.zotreex.telescope.ui.theme.onErrorDark
import ru.zotreex.telescope.ui.theme.onErrorLight
import ru.zotreex.telescope.ui.theme.onPrimaryContainerDark
import ru.zotreex.telescope.ui.theme.onPrimaryContainerLight
import ru.zotreex.telescope.ui.theme.onPrimaryDark
import ru.zotreex.telescope.ui.theme.onPrimaryLight
import ru.zotreex.telescope.ui.theme.onSecondaryContainerDark
import ru.zotreex.telescope.ui.theme.onSecondaryContainerLight
import ru.zotreex.telescope.ui.theme.onSecondaryDark
import ru.zotreex.telescope.ui.theme.onSecondaryLight
import ru.zotreex.telescope.ui.theme.onSurfaceDark
import ru.zotreex.telescope.ui.theme.onSurfaceLight
import ru.zotreex.telescope.ui.theme.onSurfaceVariantDark
import ru.zotreex.telescope.ui.theme.onSurfaceVariantLight
import ru.zotreex.telescope.ui.theme.onTertiaryContainerDark
import ru.zotreex.telescope.ui.theme.onTertiaryContainerLight
import ru.zotreex.telescope.ui.theme.onTertiaryDark
import ru.zotreex.telescope.ui.theme.onTertiaryLight
import ru.zotreex.telescope.ui.theme.primaryContainerDark
import ru.zotreex.telescope.ui.theme.primaryContainerLight
import ru.zotreex.telescope.ui.theme.primaryDark
import ru.zotreex.telescope.ui.theme.primaryLight
import ru.zotreex.telescope.ui.theme.scrimDark
import ru.zotreex.telescope.ui.theme.scrimLight
import ru.zotreex.telescope.ui.theme.secondaryContainerDark
import ru.zotreex.telescope.ui.theme.secondaryContainerLight
import ru.zotreex.telescope.ui.theme.secondaryDark
import ru.zotreex.telescope.ui.theme.secondaryLight
import ru.zotreex.telescope.ui.theme.surfaceDark
import ru.zotreex.telescope.ui.theme.surfaceLight
import ru.zotreex.telescope.ui.theme.surfaceVariantDark
import ru.zotreex.telescope.ui.theme.surfaceVariantLight
import ru.zotreex.telescope.ui.theme.tertiaryContainerDark
import ru.zotreex.telescope.ui.theme.tertiaryContainerLight
import ru.zotreex.telescope.ui.theme.tertiaryDark
import ru.zotreex.telescope.ui.theme.tertiaryLight

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
)

@Composable
fun TelescopeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkScheme
        else -> lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TypographyLocal,
        content = content
    )
}

