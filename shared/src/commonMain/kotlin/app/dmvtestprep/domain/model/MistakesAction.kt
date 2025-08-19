package app.dmvtestprep.domain.model

data class MistakesAction(
    val text: String,
    val isVisible: Boolean,
    val onClick: () -> Unit
) {
    companion object {
        fun retry(hasMistakes: Boolean, correctAnswers: Int, onClick: () -> Unit) = MistakesAction(
            text = "Retry mistakes",
            isVisible = hasMistakes && correctAnswers > 0,
            onClick = onClick
        )

        fun review(hasMistakes: Boolean, onClick: () -> Unit) = MistakesAction(
            text = "Review mistakes",
            isVisible = hasMistakes,
            onClick = onClick
        )
    }
}