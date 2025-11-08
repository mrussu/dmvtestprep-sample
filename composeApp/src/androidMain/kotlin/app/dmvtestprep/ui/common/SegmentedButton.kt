package app.dmvtestprep.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <T> SegmentedButton(
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    getLabel: (T) -> String
) {
     Surface(
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)
                .padding(3.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            options.forEach { option ->
                TextButton(
                    onClick = { onOptionSelected(option) },
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (option == selectedOption) MaterialTheme.colors.primary else Color.Transparent,
                        contentColor = if (option == selectedOption) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
                    ),
                    modifier = Modifier
                        .height(36.dp)
                        .weight(1f)
                ) {
                    BasicText(
                        text = getLabel(option),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = if (option == selectedOption) FontWeight.Medium else FontWeight.Normal,
                            color = if (option == selectedOption) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
                        ),
                        maxLines = 1,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 8.sp,
                            maxFontSize = 16.sp,
                            stepSize = 1.sp
                        ),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}