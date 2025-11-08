package app.dmvtestprep.ui.common

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dmvtestprep.backgroundColor
import app.dmvtestprep.domain.model.AnswerChar
import app.dmvtestprep.domain.model.AnswerColorProvider
import app.dmvtestprep.domain.model.ListNavigator
import app.dmvtestprep.domain.model.Question
import app.dmvtestprep.imageUrl
import app.dmvtestprep.lsBackgroundColor
import app.dmvtestprep.lsLabel
import app.dmvtestprep.lsTextColor
import kotlin.math.abs

@Composable
fun QuestionsDetailView(
    answers: Map<Int, AnswerChar.Value>,
    questions: ListNavigator<Question>,
    showIncorrectAnswers: Boolean,
    englishMode: Boolean,
    onToggleSave: (Int) -> Unit,
    onQuestionPageChanged: (Int) -> Unit
) {
    val pagerState = rememberPagerState(
        pageCount = { questions.size },
        initialPage = questions.currentIndex
    )

    VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        flingBehavior = PagerDefaults.flingBehavior(
            state = pagerState,
            pagerSnapDistance = PagerSnapDistance.atMost(1),
            decayAnimationSpec = rememberSplineBasedDecay(),
            snapAnimationSpec = spring(
                stiffness = Spring.StiffnessMediumLow,
                visibilityThreshold = Int.VisibilityThreshold.toFloat()
            ),
            snapPositionalThreshold = 0.2f
        ),
        pageSpacing = 0.dp
    ) { page ->
        val normalizedOffset = abs((pagerState.currentPage - page + pagerState.currentPageOffsetFraction)).coerceIn(0f, 1f)
        val scale = 0.85f + (1 - abs(normalizedOffset)) * 0.15f
        val cornerFraction = (abs(pagerState.currentPageOffsetFraction) / 0.1f).coerceIn(0f, 1f)
        val cornerRadius = 8.dp * cornerFraction

        val question = questions[page]
        val selectedAnswerChar = answers[question.id] ?: question.correctAnswerChar

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(cornerRadius),
                    clip = false
                )
                .clip(RoundedCornerShape(cornerRadius))
                .border(
                    border = BorderStroke(1.dp, MaterialTheme.colors.surface.copy(alpha = 0.75f)),
                    shape = RoundedCornerShape(cornerRadius)
                )
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(cornerRadius)
                )
        ) {
            QuestionView(
                question = question,
                selectedAnswerChar = selectedAnswerChar,
                showIncorrectAnswers = showIncorrectAnswers,
                englishMode = englishMode,
                onToggleSave = onToggleSave
            )
        }
    }
    LaunchedEffect(pagerState.currentPage) {
        onQuestionPageChanged(pagerState.currentPage)
    }
}

@Composable
fun QuestionView(
    question: Question,
    selectedAnswerChar: AnswerChar.Value,
    showIncorrectAnswers: Boolean,
    englishMode: Boolean,
    onToggleSave: (Int) -> Unit
) {
    var showIncorrectAnswersState by remember { mutableStateOf(showIncorrectAnswers) }
    val toggleText = if (showIncorrectAnswersState) "Hide incorrect answers" else "Show incorrect answers"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(question.backgroundColor)
            .padding(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = question.lsLabel,
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = question.lsTextColor,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(question.lsBackgroundColor)
                        .padding(8.dp, 6.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                ToggleSavedButton(
                    questionId = question.id,
                    isSaved = question.isSaved,
                    modifier = Modifier.size(32.dp),
                    onToggleSave = onToggleSave
                )
            }
            Text(
                text = question.getText(englishMode),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(36.dp, 12.dp)
            )
            question.imageUrl?.let { imageUrl ->
                QuestionImageView(imageUrl = imageUrl)
            }
            TextLink(toggleText, 14, FontWeight.Normal) {
                showIncorrectAnswersState = !showIncorrectAnswersState
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(if (showIncorrectAnswersState) 8.dp else 0.dp)
            ) {
                question.answers.forEach { answer ->
                    val isCorrect =  answer.char == question.correctAnswerChar.char
                    val colorScheme = AnswerColorProvider.getColorScheme(
                        char = answer.char,
                        selectedAnswerChar = selectedAnswerChar,
                        correctAnswerChar = question.correctAnswerChar,
                        isSubmitted = true
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        if (showIncorrectAnswersState || isCorrect) {
                            AnswerItemView(
                                answer = answer,
                                englishMode = englishMode,
                                colorScheme = colorScheme
                            )
                        }
                    }
                }
            }
        }
    }
}