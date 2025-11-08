package app.dmvtestprep

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import app.dmvtestprep.core.ui.model.ColorModel
import app.dmvtestprep.core.ui.model.ThemeColor
import app.dmvtestprep.domain.model.Question
import app.dmvtestprep.datasource.api.ApiConfig
import app.dmvtestprep.domain.model.MarkdownText

val Question.lsLabel: String
    get() = num.formatted(learningStage.type.value)

val Question.lsTextColor: Color
    @Composable
    get() = learningStage.getTextColor().asColor()

val Question.lsBackgroundColor: Color
    @Composable
    get() = learningStage.getBackgroundColor().asColor()

val Question.backgroundColor: Color
    @Composable
    get() = learningStage.getBackgroundColor().asColor(alpha = 0.1f)

val Question.imageUrl: String?
    get() = image?.toFullImageUrl()

@Composable
fun ThemeColor.asColor(alpha: Float? = null): Color {
    val isDark = isSystemInDarkTheme()
    val colorModel = if (isDark) dark else light

    return colorModel.asColor(alpha ?: this@asColor.alpha)
}

fun ColorModel.asColor(alpha: Float? = null): Color {
    return Color(r, g, b, alpha ?: a)
}

@Composable
fun AnnotatedString.Builder.AppendMarkdownTexts(texts: List<MarkdownText>) {
    texts.forEach {
        when (it) {
            is MarkdownText.Bold -> withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                append(it.text)
            }
            is MarkdownText.Italic -> withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                append(it.text)
            }
            is MarkdownText.Link -> withLink(LinkAnnotation.Url(url = it.url)) {
                withStyle(SpanStyle(textDecoration = TextDecoration.Underline, color = it.color.asColor())) {
                    append(it.text)
                }
            }
            is MarkdownText.Plain -> append(it.text)
        }
    }
}

fun String?.toFullImageUrl(): String? {
    return ApiConfig.getFullImageUrl(path = this)
}