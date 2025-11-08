package app.dmvtestprep.ui.common

import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import app.dmvtestprep.ui.icons.Icons

@Composable
fun ToggleEnglishModeButton(
    englishMode: Boolean,
    isNativeAvailable: Boolean,
    onToggleEnglishMode: () -> Unit
) {
    IconToggleButton(
        checked = englishMode,
        onCheckedChange = { onToggleEnglishMode() },
        enabled = isNativeAvailable
    ) {
        Icon(
            painter = if (englishMode) Icons.EnglishModeFilled else Icons.EnglishModeOutlined,
            contentDescription = if (englishMode) "Switch to English" else "Switch to selected language",
            tint = if (englishMode) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onPrimary.copy(alpha = ContentAlpha.medium)
        )
    }
}