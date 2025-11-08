package app.dmvtestprep.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LargeButton(
    text: String,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        shape = MaterialTheme.shapes.medium,
        onClick = onClick,
        enabled = isEnabled,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = text,
            fontSize = 14.sp
        )
    }
}