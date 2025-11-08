package app.dmvtestprep.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.dmvtestprep.ui.common.QuestionView

@Preview
@Composable
fun PreviewLearnQuestionView() {
    QuestionView(
        question = MockQuestion.question167,
        selectedAnswerChar = MockQuestion.question167.correctAnswerChar,
        showIncorrectAnswers = true,
        englishMode = true,
        onToggleSave = {}
    )
}