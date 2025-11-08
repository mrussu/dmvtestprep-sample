package app.dmvtestprep.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .border(
                width = 2.dp,
                color = colorScheme.border.asColor(),
                shape = MaterialTheme.shapes.medium
            )
            .background(
                color = colorScheme.background.asColor(),
                shape = MaterialTheme.shapes.medium
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