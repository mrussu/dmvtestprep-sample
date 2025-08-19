package app.dmvtestprep.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.dmvtestprep.domain.model.AnswerState
import app.dmvtestprep.domain.model.Settings
import app.dmvtestprep.domain.model.TestState.TestOngoing
import app.dmvtestprep.domain.model.toAnswerChar
import app.dmvtestprep.ui.screen.TestOngoingView

@Preview
@Composable
fun PreviewTestOngoingView() {
    val uiState = TestOngoing(
        id = 1,
        test = MockTest.test1,
        answerState = AnswerState(
            correctAnswer = 'C'.toAnswerChar(),
            selectedAnswer = 'B'.toAnswerChar(),
            isSubmitted = true
        ),
        questionIndex = 0,
        settings = Settings.default()
    )

    TestOngoingView(
        uiState = uiState,
        onToggleSave = {},
        onSelectAnswer = {_, _ ->},
        onActionButtonClick = {}
    )
}