package app.dmvtestprep.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.dmvtestprep.ui.screen.QuestionItem

@Preview
@Composable
fun PreviewQuestionItem() {
    QuestionItem(
        question = MockQuestion.question25,
        onToggleSave = {},
        englishMode = true,
        onClick = {}
    )
}