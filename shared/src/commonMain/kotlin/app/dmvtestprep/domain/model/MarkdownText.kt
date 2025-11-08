package app.dmvtestprep.domain.model

import app.dmvtestprep.core.ui.model.ThemeColor
import app.dmvtestprep.core.ui.theme.AppColors

sealed class MarkdownText {
    abstract val text: String

    data class Plain(override val text: String) : MarkdownText()
    data class Bold(override val text: String) : MarkdownText()
    data class Italic(override val text: String) : MarkdownText()
    data class Link(
        override val text: String,
        val url: String,
        val color: ThemeColor = AppColors.link
    ) : MarkdownText()
}