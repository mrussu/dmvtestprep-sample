package app.dmvtestprep.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dmvtestprep.asColor
import app.dmvtestprep.domain.model.ColorModel
import app.dmvtestprep.domain.settings.Setting
import app.dmvtestprep.ui.theme.LightUnderlay

@Composable
fun SettingToggle(
    setting: Setting<Boolean>,
    onToggle: (Boolean) -> Unit,
    isLabelBold: Boolean = false,
    labelIcon: @Composable (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 48.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = setting.label,
                    fontSize = 16.sp,
                    fontWeight = if (isLabelBold) FontWeight.Medium else FontWeight.Normal,
                    lineHeight = 32.sp
                )
                labelIcon?.invoke()
            }
            if (setting.details.isNotBlank()) {
                Text(
                    text = setting.details,
                    fontSize = 15.sp,
                    color = ColorModel.gray.asColor(alpha = 0.75f)
                )
            }
        }
        Switch(
            checked = setting.value,
            enabled = setting.isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary,
                uncheckedThumbColor = MaterialTheme.colors.secondary,
                checkedTrackColor = Color.Gray,
                uncheckedTrackColor = LightUnderlay
            ),
            modifier = Modifier
                .height(48.dp)
        )
    }
}