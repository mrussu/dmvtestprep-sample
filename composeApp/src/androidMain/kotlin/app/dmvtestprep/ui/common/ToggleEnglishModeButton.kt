package app.dmvtestprep.ui.common

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import app.dmvtestprep.asColor
import app.dmvtestprep.domain.model.ColorModel
import app.dmvtestprep.ui.icons.Icons

@Composable
fun ToggleEnglishModeButton(
    englishMode: Boolean,
    isNativeAvailable: Boolean,
    onToggleEnglishMode: () -> Unit
) {
    IconButton(
        onClick = { onToggleEnglishMode() },
        enabled = isNativeAvailable
    ) {
        Icon(
            painter = Icons.EnglishMode,
            contentDescription = if (englishMode) {
                "Switch to English"
            } else {
                "Switch to selected language"
            },
            tint = if (englishMode) {
                ColorModel.softYellow.asColor()
            } else {
                ColorModel.white.asColor()
            }
        )
    }
}