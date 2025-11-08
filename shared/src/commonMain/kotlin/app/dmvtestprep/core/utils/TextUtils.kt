package app.dmvtestprep.core.utils

import app.dmvtestprep.domain.model.MarkdownLine
import app.dmvtestprep.domain.model.MarkdownText

fun String.toMarkdown() = lines().mapNotNull { parseMarkdownLine(it.trim()) }

private fun parseMarkdownLine(line: String): MarkdownLine? {
    val headerRegex = Regex("^(#{1,3})\\s+(.*)")
    val listRegex = Regex("^(\\d+\\.|[-•])\\s+(.*)")

    headerRegex.find(line)?.let { match ->
        val (level, text) = match.destructured
        return MarkdownLine.Header(text = text, level = level.length)
    }

    listRegex.find(line)?.let { match ->
        val (marker, text) = match.destructured
        return MarkdownLine.ListItem(
            marker = if (marker.endsWith(".")) marker else "•",
            segments = parseMarkdownText(text)
        )
    }

    if (line.isNotEmpty()) {
        return MarkdownLine.Paragraph(segments = parseMarkdownText(line))
    }

    return null
}

private fun parseMarkdownText(text: String): List<MarkdownText> {
    val result = mutableListOf<MarkdownText>()
    val styleRegex = Regex("\\*\\*(.*?)\\*\\*|\\*(.*?)\\*|\\[(.*?)]\\((.*?)\\)")

    var lastIndex = 0

    styleRegex.findAll(text).forEach { match ->
        if (match.range.first > lastIndex) {
            result.add(MarkdownText.Plain(text.substring(lastIndex, match.range.first)))
        }

        match.groups[1]?.let { result.add(MarkdownText.Bold(it.value)) }
        match.groups[2]?.let { result.add(MarkdownText.Italic(it.value)) }
        match.groups[3]?.let { linkText ->
            match.groups[4]?.let { linkUrl ->
                result.add(MarkdownText.Link(linkText.value, linkUrl.value))
            }
        }

        lastIndex = match.range.last + 1
    }

    if (lastIndex < text.length) {
        result.add(MarkdownText.Plain(text.substring(lastIndex)))
    }

    return result
}