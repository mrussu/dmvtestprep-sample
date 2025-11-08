package app.dmvtestprep.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.dmvtestprep.asColor
import app.dmvtestprep.domain.model.AnswerChar
import app.dmvtestprep.domain.model.TestEventHandlerFactory
import app.dmvtestprep.domain.model.TestState
import app.dmvtestprep.domain.model.ViewMode
import app.dmvtestprep.domain.model.toAnswerChar
import app.dmvtestprep.domain.usecase.UseCaseProvider
import app.dmvtestprep.imageUrl
import app.dmvtestprep.ui.common.AnswerItemView
import app.dmvtestprep.ui.common.ConfirmAlertDialog
import app.dmvtestprep.ui.common.ErrorView
import app.dmvtestprep.ui.common.LargeButton
import app.dmvtestprep.ui.common.QuestionImageView
import app.dmvtestprep.ui.common.QuestionsDetailView
import app.dmvtestprep.ui.common.ToggleEnglishModeButton
import app.dmvtestprep.ui.common.ToggleSavedButton
import app.dmvtestprep.viewmodel.TestViewModel
import app.dmvtestprep.ui.icons.Icons
import app.dmvtestprep.viewmodel.actions.TestCompletedActions
import app.dmvtestprep.viewmodel.actions.TestCompletedActionsHandler

@Composable
fun TestScreen(onDismiss: () -> Unit) {
    var showExitConfirmation by remember { mutableStateOf(false) }

    val viewModel = remember {
        TestViewModel(
            TestEventHandlerFactory.create(),
            UseCaseProvider.getTestUseCase,
            UseCaseProvider.toggleSavedQuestionUseCase,
            UseCaseProvider.settingsUseCase
        )
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onBackPressHandler = remember {
        {
            when {
                uiState.isDetailView -> viewModel.onBackToTestResult()
                uiState.isErrorOccurred -> onDismiss()
                else -> showExitConfirmation = true
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onNewTest()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clear()
        }
    }

    BackHandler {
        onBackPressHandler()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onBackPressHandler) {
                        Icon(Icons.ArrowBack, contentDescription = "Cancel")
                    }
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
                },
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (val state = uiState) {
                    is TestState.Loading,
                    is TestState.Completing -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    is TestState.TestOngoing -> {
                        TestOngoingView(
                            uiState = state,
                            onToggleSave = { viewModel.onToggleSavedQuestion(it) },
                            onSelectAnswer = { questionId, it -> viewModel.onSelectAnswer(questionId, it) },
                            onActionButtonClick = { viewModel.onActionButtonClick() }
                        )
                    }
                    is TestState.TestCompleted -> {
                        when (state.viewMode) {
                            is ViewMode.MainView -> {
                                TestCompletedView(
                                    uiState = state,
                                    actions = TestCompletedActionsHandler(
                                        onRetryCallback = { viewModel.onRetryMistakes() },
                                        onReviewCallback = { viewModel.onReviewMistakes() },
                                        onStartOverCallback = { viewModel.onStartOver() },
                                        onNewTestCallback = { viewModel.onNewTest() },
                                        onDismissCallback = onDismiss
                                    )
                                )
                            }
                            is ViewMode.DetailView -> {
                                QuestionsDetailView(
                                    answers = state.mistakes,
                                    questions = state.questions,
                                    showIncorrectAnswers = true,
                                    englishMode = state.englishMode,
                                    onToggleSave = { viewModel.onToggleSavedQuestion(it) },
                                    onQuestionPageChanged = { viewModel.onQuestionPageChanged(it) }
                                )
                            }
                        }
                    }
                    is TestState.Error -> {
                        ErrorView(
                            errorSummary = state.errorSummary,
                            onRetry = state.onRetry
                        )
                    }
                }
            }
        }
    )

    if (showExitConfirmation) {
        ConfirmAlertDialog(
            text = "Are you sure you want to finish the test?",
            confirmButtonText = "Exit",
            onDismiss = { showExitConfirmation = false },
            onConfirm = { onDismiss() }
        )
    }
}

@Composable
fun TestOngoingView(
    uiState: TestState.TestOngoing,
    onToggleSave: (Int) -> Unit,
    onSelectAnswer: (Int, AnswerChar.Value) -> Unit,
    onActionButtonClick: () -> Unit
) {
    val question = uiState.question
    val answerState = uiState.answerState

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            item {  }
            item {
                Text(
                    text = question.getText(uiState.englishMode),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(36.dp, 12.dp)
                )
            }
            question.imageUrl?.let { imageUrl ->
                item {
                    QuestionImageView(imageUrl = imageUrl)
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(0.dp, 12.dp, 0.dp, 72.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    question.answers.forEach { answer ->
                        AnswerItemView(
                            answer = answer,
                            englishMode = uiState.englishMode,
                            colorScheme = answerState.getColorScheme(answer.char),
                            modifier = Modifier.clickable(enabled = !answerState.isSubmitted) {
                                onSelectAnswer(question.id, answer.char.toAnswerChar())
                            }
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            ToggleSavedButton(
                questionId = question.id,
                isSaved = question.isSaved,
                modifier = Modifier.size(32.dp),
                onToggleSave = onToggleSave
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colors.background)
                .padding(12.dp)
        ) {
            LargeButton(
                text = uiState.actionButtonText,
                isEnabled = answerState.isSelected
            ) {
                onActionButtonClick()
            }
        }
    }
}

@Composable
fun TestCompletedView(
    uiState: TestState.TestCompleted,
    actions: TestCompletedActions
) {
    val mistakesAction = actions.createMistakesAction(
        testMode = uiState.testMode,
        hasMistakes = uiState.mistakes.isNotEmpty(),
        correctAnswers = uiState.testResult.correctAnswers
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(uiState.testResult.backgroundColor.asColor())
            .padding(36.dp, 12.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = uiState.testResult.header.text,
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = uiState.testResult.header.color.asColor()
        )
        Text(
            text = uiState.testResult.detail.text,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = uiState.testResult.detail.color.asColor()
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = uiState.testResult.inspire.text,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = uiState.testResult.inspire.color.asColor()
        )
        if (mistakesAction.isVisible) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { mistakesAction.onClick() }
            ) {
                Text(
                    modifier = Modifier.padding(6.dp, 3.dp),
                    text = mistakesAction.text,
                    fontSize = 13.sp
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { actions.onStartOver() }
        ) {
            Text(
                modifier = Modifier.padding(6.dp, 3.dp),
                text = "Start over",
                fontSize = 13.sp
            )
        }
        Button(
            onClick = { actions.onNewTest() }
        ) {
            Text(
                modifier = Modifier.padding(12.dp, 8.dp),
                text = "New test",
                fontSize = 16.sp
            )
        }
        TextButton(
            onClick = { actions.onDismiss() }
        ) {
            Text(
                text = "Next time",
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}