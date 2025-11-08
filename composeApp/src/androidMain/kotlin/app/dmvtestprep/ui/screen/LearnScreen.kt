package app.dmvtestprep.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.dmvtestprep.backgroundColor
import app.dmvtestprep.domain.model.QuestionFilter
import app.dmvtestprep.domain.model.LearnState
import app.dmvtestprep.domain.model.Question
import app.dmvtestprep.domain.model.ViewMode
import app.dmvtestprep.domain.usecase.UseCaseProvider
import app.dmvtestprep.imageUrl
import app.dmvtestprep.lsBackgroundColor
import app.dmvtestprep.lsLabel
import app.dmvtestprep.lsTextColor
import app.dmvtestprep.ui.common.AsyncImageView
import app.dmvtestprep.ui.common.ErrorView
import app.dmvtestprep.ui.common.QuestionsDetailView
import app.dmvtestprep.ui.common.SegmentedButton
import app.dmvtestprep.ui.common.ToggleEnglishModeButton
import app.dmvtestprep.ui.common.ToggleSavedButton
import app.dmvtestprep.ui.icons.Icons
import app.dmvtestprep.viewmodel.LearnViewModel

@Composable
fun LearnScreen(onBottomNavVisibilityChanged: (Boolean) -> Unit) {
    val viewModel = remember {
        LearnViewModel(
            UseCaseProvider.getQuestionsUseCase,
            UseCaseProvider.toggleSavedQuestionUseCase,
            UseCaseProvider.settingsUseCase
        )
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isDetailView) {
        onBottomNavVisibilityChanged(!uiState.isDetailView)
    }

    LaunchedEffect(Unit) {
        viewModel.loadQuestions()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clear()
        }
    }

    BackHandler(enabled = uiState.isDetailView) {
        viewModel.onBackToQuestionList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = when {
                    uiState.isDetailView -> {
                        {
                            IconButton(
                                onClick = { viewModel.onBackToQuestionList() }
                            ) {
                                Icon(Icons.ArrowBack, contentDescription = "Back")
                            }
                        }
                    }
                    else -> null
                },
                title = {
                    Text(text = uiState.title)
                },
                actions = {
                    if (uiState.isNativeAvailable) {
                        ToggleEnglishModeButton(
                            englishMode = uiState.englishMode,
                            isNativeAvailable = uiState.isNativeAvailable
                        ) {
                            viewModel.onToggleEnglishMode()
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (val state = uiState) {
                    is LearnState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    is LearnState.Learn -> {
                        when (state.viewMode) {
                            is ViewMode.MainView -> {
                                QuestionsView(
                                    uiState = state,
                                    onSetFilter = { viewModel.onSetFilter(it) },
                                    onToggleSave = { viewModel.onToggleSavedQuestion(it) },
                                    onQuestionDetail = { viewModel.onQuestionDetail(it) },
                                    onRefresh = { viewModel.reloadQuestions() }
                                )
                            }
                            is ViewMode.DetailView -> {
                                QuestionsDetailView(
                                    answers = emptyMap(),
                                    questions = state.questions,
                                    showIncorrectAnswers = false,
                                    englishMode = state.englishMode,
                                    onToggleSave = { viewModel.onToggleSavedQuestion(it) },
                                    onQuestionPageChanged = { viewModel.onQuestionPageChanged(it) }
                                )
                            }
                        }
                    }
                    is LearnState.Error -> {
                        ErrorView(
                            errorSummary = state.errorSummary,
                            onRetry = state.onRetry
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QuestionsView(
    uiState: LearnState.Learn,
    onSetFilter: (QuestionFilter) -> Unit,
    onToggleSave: (Int) -> Unit,
    onQuestionDetail: (Int) -> Unit,
    onRefresh: () -> Unit
) {
    val questions by remember(uiState.questions) {
        derivedStateOf { uiState.questions }
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isProcessing,
        onRefresh = { onRefresh() }
    )

    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp, 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                SegmentedButton(
                    options = QuestionFilter.entries,
                    selectedOption = uiState.filter,
                    onOptionSelected = onSetFilter,
                    getLabel = { it.filterName }
                )
            }
            itemsIndexed(questions.toList(), key = { _, question -> question.id }) { index, question ->
                QuestionItem(
                    question = question,
                    englishMode = uiState.englishMode,
                    onToggleSave = onToggleSave
                ) {
                    onQuestionDetail(index)
                }
            }
        }

        PullRefreshIndicator(
            refreshing = uiState.isProcessing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun QuestionItem(
    question: Question,
    englishMode: Boolean,
    onToggleSave: (Int) -> Unit,
    onClick: () -> Unit
) {
    val lineHeightSp = 20.sp
    val heightInDp = with(LocalDensity.current) { lineHeightSp.toDp() }

    Surface(
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colors.surface.copy(alpha = 0.75f)),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(question.backgroundColor)
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = question.lsLabel,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = question.lsTextColor,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(question.lsBackgroundColor)
                        .padding(6.dp, 4.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                ToggleSavedButton(
                    questionId = question.id,
                    isSaved = question.isSaved,
                    modifier = Modifier.size(20.dp),
                    onToggleSave = onToggleSave
                )
            }
            Row(
                modifier = Modifier
                    .height(heightInDp * 3)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question.getText(englishMode),
                    fontSize = 16.sp,
                    lineHeight = lineHeightSp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                )
                question.imageUrl?.let { imageUrl ->
                    AsyncImageView(
                        imageUrl = imageUrl,
                        modifier = Modifier
                            .width(heightInDp * 3)
                            .height(heightInDp * 3)
                    )
                }
            }
        }
    }
}