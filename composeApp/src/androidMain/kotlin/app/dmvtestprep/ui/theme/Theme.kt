package app.dmvtestprep.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import app.dmvtestprep.asColor
import app.dmvtestprep.core.ui.theme.AppColors

val LightColorPalette = lightColors(
    primary = AppColors.primary.light.asColor(),
    primaryVariant = AppColors.primary.light.asColor(),
    secondary = AppColors.secondary.light.asColor(),
    secondaryVariant = AppColors.secondary.light.asColor(),
    background = AppColors.background.light.asColor(),
    surface = AppColors.surface.light.asColor(),
    error = AppColors.error.light.asColor(),
    onPrimary = AppColors.onPrimary.light.asColor(),
    onSecondary = AppColors.onSecondary.light.asColor(),
    onBackground = AppColors.onBackground.light.asColor(),
    onSurface = AppColors.onSurface.light.asColor(),
    onError = AppColors.onError.light.asColor()
)

val DarkColorPalette = darkColors(
    primary = AppColors.primary.dark.asColor(),
    primaryVariant = AppColors.primary.dark.asColor(),
    secondary = AppColors.secondary.dark.asColor(),
    secondaryVariant = AppColors.secondary.dark.asColor(),
    background = AppColors.background.dark.asColor(),
    surface = AppColors.surface.dark.asColor(),
    error = AppColors.error.dark.asColor(),
    onPrimary = AppColors.onPrimary.dark.asColor(),
    onSecondary = AppColors.onSecondary.dark.asColor(),
    onBackground = AppColors.onBackground.dark.asColor(),
    onSurface = AppColors.onSurface.dark.asColor(),
    onError = AppColors.onError.dark.asColor(),
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        shapes = Shapes(
            small = RoundedCornerShape(6.dp),
            medium = RoundedCornerShape(8.dp),
            large = RoundedCornerShape(0.dp)
        ),
        content = content
    )
}