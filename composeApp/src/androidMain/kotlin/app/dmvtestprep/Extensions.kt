package app.dmvtestprep

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import app.dmvtestprep.domain.model.ColorModel
import app.dmvtestprep.domain.model.Question
import app.dmvtestprep.datasource.api.ApiConfig
import app.dmvtestprep.domain.model.MarkdownText

val Question.lsLabel: String
    get() = num.formatted(learningStage.type.value)

val Question.lsTextColor: Color
    get() = learningStage.getTextColor().asColor()

val Question.lsBackgroundColor: Color
    get() = learningStage.getBackgroundColor().asColor()

val Question.backgroundColor: Color
    get() = learningStage.getBackgroundColor().asColor(alpha = 0.1f)

val Question.imageUrl: String?
    get() = image?.toFullImageUrl()

fun ColorModel.asColor(alpha: Float? = null): Color {
    return Color(
        red = this.r,
        green = this.g,
        blue = this.b,
        alpha = alpha ?: this.a
    )
}

fun AnnotatedString.Builder.appendMarkdownTexts(texts: List<MarkdownText>) {
    texts.forEach {
        when (it) {
            is MarkdownText.Bold -> withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                append(it.text)
            }
            is MarkdownText.Italic -> withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                append(it.text)
            }
            is MarkdownText.Link -> withLink(LinkAnnotation.Url(url = it.url)) {
                withStyle(SpanStyle(fontWeight = FontWeight.Medium, color = it.color.asColor())) {
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