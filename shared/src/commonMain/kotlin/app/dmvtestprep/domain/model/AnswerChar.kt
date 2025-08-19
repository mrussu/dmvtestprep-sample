package app.dmvtestprep.domain.model

sealed class AnswerChar {
    open val char: Char = ' '
    data object None : AnswerChar()
    data class Value(override val char: Char) : AnswerChar()
}