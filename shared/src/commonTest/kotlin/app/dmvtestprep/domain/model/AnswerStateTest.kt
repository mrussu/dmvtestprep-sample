package app.dmvtestprep.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class AnswerStateTest {
    @Test
    fun testGetColorScheme_UserHasNotSelectedAnswer() {
        val state = AnswerState(
            correctAnswer = AnswerChar.Value('A'),
            selectedAnswer = AnswerChar.None,
            isSubmitted = false
        )
        val result = state.getColorScheme('A')
        assertEquals(AnswerColorProvider.ColorScheme.DEFAULT, result)
    }

    @Test
    fun testGetColorScheme_UserSelectedButNotSubmitted() {
        val state = AnswerState(
            correctAnswer = AnswerChar.Value('A'),
            selectedAnswer = AnswerChar.Value('A'),
            isSubmitted = false
        )
        val selectedResult = state.getColorScheme('A')
        assertEquals(AnswerColorProvider.ColorScheme.SELECTED, selectedResult)

        val otherResult = state.getColorScheme('B')
        assertEquals(AnswerColorProvider.ColorScheme.DEFAULT, otherResult)
    }

    @Test
    fun testGetColorScheme_UserSelectedCorrectAnswerButNotSubmitted() {
        val state = AnswerState(
            correctAnswer = AnswerChar.Value('A'),
            selectedAnswer = AnswerChar.Value('A'),
            isSubmitted = false
        )
        val selectedResult = state.getColorScheme('A')
        assertEquals(AnswerColorProvider.ColorScheme.SELECTED, selectedResult)

        val otherResult = state.getColorScheme('B')
        assertEquals(AnswerColorProvider.ColorScheme.DEFAULT, otherResult)
    }

    @Test
    fun testGetColorScheme_UserSelectedIncorrectAnswerButNotSubmitted() {
        val state = AnswerState(
            correctAnswer = AnswerChar.Value('A'),
            selectedAnswer = AnswerChar.Value('B'),
            isSubmitted = false
        )
        val selectedResult = state.getColorScheme('B')
        assertEquals(AnswerColorProvider.ColorScheme.SELECTED, selectedResult)

        val otherResult = state.getColorScheme('A')
        assertEquals(AnswerColorProvider.ColorScheme.DEFAULT, otherResult)
    }

    @Test
    fun testGetColorScheme_UserSelectedCorrectAnswerAndSubmitted() {
        val state = AnswerState(
            correctAnswer = AnswerChar.Value('A'),
            selectedAnswer = AnswerChar.Value('A'),
            isSubmitted = true
        )
        val correctResult = state.getColorScheme('A')
        assertEquals(AnswerColorProvider.ColorScheme.CORRECT, correctResult)

        val unselectedResult = state.getColorScheme('B')
        assertEquals(AnswerColorProvider.ColorScheme.UNSELECTED, unselectedResult)
    }

    @Test
    fun testGetColorScheme_UserSelectedIncorrectAnswerAndSubmitted() {
        val state = AnswerState(
            correctAnswer = AnswerChar.Value('A'),
            selectedAnswer = AnswerChar.Value('B'),
            isSubmitted = true
        )
        val incorrectResult = state.getColorScheme('B')
        assertEquals(AnswerColorProvider.ColorScheme.INCORRECT, incorrectResult)

        val correctResult = state.getColorScheme('A')
        assertEquals(AnswerColorProvider.ColorScheme.HIGHLIGHT, correctResult)

        val unselectedResult = state.getColorScheme('C')
        assertEquals(AnswerColorProvider.ColorScheme.UNSELECTED, unselectedResult)
    }
}