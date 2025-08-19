package app.dmvtestprep.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.dmvtestprep.asColor
import app.dmvtestprep.domain.model.AnswerColorProvider
import app.dmvtestprep.domain.model.Question

@Composable
fun AnswerItemView(
    answer: Question.Answer,
    englishMode: Boolean,
    colorScheme: AnswerColorProvider.ColorScheme,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(6.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .border(
                width = 2.dp,
                color = colorScheme.border.asColor(),
                shape = shape
            )
            .background(
                color = colorScheme.background.asColor(),
                shape = shape
            )
            .then(modifier)
            .padding(12.dp)
    ) {
        Row {
            if (answer.prefix.isNotBlank()) {
                Text(
                    text = answer.prefix,
                    color = colorScheme.text.asColor()
                )
            }
            Text(
                text = answer.getText(englishMode),
                color = colorScheme.text.asColor()
            )
        }
    }
}