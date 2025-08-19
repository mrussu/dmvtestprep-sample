package app.dmvtestprep.domain.model

sealed class MarkdownLine {
    data class Header(
        val text: String,
        val level: Int
    ) : MarkdownLine()

    data class ListItem(
        val marker: String,
        val segments: List<MarkdownText>
    ) : MarkdownLine()

    data class Paragraph(
        val segments: List<MarkdownText>
    ) : MarkdownLine()
}