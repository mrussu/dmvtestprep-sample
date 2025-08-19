package app.dmvtestprep.domain.model

sealed class MarkdownText {
    abstract val text: String

    data class Plain(override val text: String) : MarkdownText()
    data class Bold(override val text: String) : MarkdownText()
    data class Italic(override val text: String) : MarkdownText()
    data class Link(
        override val text: String,
        val url: String,
        val color: ColorModel = ColorModel.skyBlue
    ) : MarkdownText()
}