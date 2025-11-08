package app.dmvtestprep.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.dmvtestprep.AppendMarkdownTexts
import app.dmvtestprep.core.config.AppInfo
import app.dmvtestprep.domain.model.DocumentState
import app.dmvtestprep.domain.model.FeedbackState
import app.dmvtestprep.domain.model.Language
import app.dmvtestprep.domain.model.MarkdownLine
import app.dmvtestprep.domain.model.SettingsState
import app.dmvtestprep.domain.model.SettingsViews
import app.dmvtestprep.domain.resources.Texts
import app.dmvtestprep.domain.usecase.UseCaseProvider
import app.dmvtestprep.ui.common.ErrorView
import app.dmvtestprep.ui.common.LargeButton
import app.dmvtestprep.ui.common.SettingToggle
import app.dmvtestprep.ui.common.TextLink
import app.dmvtestprep.ui.icons.Icons
import app.dmvtestprep.viewmodel.SettingsViewModel
import app.dmvtestprep.viewmodel.actions.SettingsActionsHandler

@Composable
fun SettingsScreen(
    onBottomNavVisibility: (Boolean) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel = remember {
        SettingsViewModel(
            UseCaseProvider.getLanguagesUseCase,
            UseCaseProvider.getDocumentUseCase,
            UseCaseProvider.sendFeedbackUseCase,
            UseCaseProvider.settingsUseCase,
            UseCaseProvider.settingFactory
        )
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onBackPressHandler = remember {
        {
            when (uiState.views) {
                is SettingsViews.SettingsView -> {
                    onBackClick()
                }
                is SettingsViews.FeedbackView,
                is SettingsViews.DocumentView -> {
                    viewModel.onSettingsView()
                }
            }
        }
    }

    LaunchedEffect(uiState.views) {
        onBottomNavVisibility(uiState.views is SettingsViews.SettingsView)
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
                title = {
                    Text(
                        text = uiState.views.title
                    )
                },
                navigationIcon = {
                    IconButton(onBackPressHandler) {
                        Icon(Icons.ArrowBack, contentDescription = "Back")
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
                when (val state = uiState.views) {
                    is SettingsViews.SettingsView -> {
                        SettingsContent(
                            uiState = uiState,
                            actions = SettingsActionsHandler(
                                onSelectLanguageCallback = { viewModel.onSelectLanguage(it) },
                                onEnglishModeCallback = { viewModel.onToggleEnglishMode(it) },
                                onAnswerPrefixCallback = { viewModel.onAnswerPrefix(it) },
                                onFeedbackViewCallback = { viewModel.onFeedbackView() },
                                onDocumentViewCallback = { viewModel.onDocumentView(it) }
                            )
                        )
                    }
                    is SettingsViews.FeedbackView -> {
                        when (val feedbackState = state.uiState) {
                            is FeedbackState.Message -> {
                                FeedbackView(
                                    message = feedbackState,
                                    onChange = { viewModel.onMessageChange(it) },
                                    onSend = { viewModel.onSendFeedback() }
                                )
                            }
                            is FeedbackState.Sending -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            is FeedbackState.Sent -> {
                                FeedbackSent(
                                    onClose = { viewModel.onSettingsView() }
                                )
                            }
                            is FeedbackState.Error -> {
                                ErrorView(
                                    errorSummary = feedbackState.errorSummary,
                                    onRetry = feedbackState.onRetry
                                )
                            }
                        }
                    }
                    is SettingsViews.DocumentView -> {
                        when (val documentState = state.uiState) {
                            is DocumentState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            is DocumentState.Document -> {
                                DocumentContent(
                                    document = documentState
                                )
                            }
                            is DocumentState.Error -> {
                                ErrorView(
                                    errorSummary = documentState.errorSummary,
                                    onRetry = documentState.onRetry
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun SettingsContent(
    uiState: SettingsState,
    actions: SettingsActionsHandler
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Test language",
                    fontWeight = FontWeight.Medium
                )
                OutlinedButton(
                    onClick = { showDialog = true },
                    enabled = uiState.languages.isNotEmpty(),
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = uiState.languageLabel)
                }
            }
            SettingToggle(
                setting = uiState.settings.englishMode,
                onToggle = actions.onToggleEnglishMode,
                isLabelBold = true
            ) {
                Icon(
                    painter = if (uiState.settings.englishMode.value) {
                        Icons.EnglishModeFilled
                    } else {
                        Icons.EnglishModeOutlined
                    },
                    contentDescription = if (uiState.settings.englishMode.value) {
                        "Switch to English"
                    } else {
                        "Switch to selected language"
                    }
                )
            }
            SettingToggle(
                setting = uiState.settings.answerPrefix,
                onToggle = actions.onAnswerPrefix,
                isLabelBold = true
            )
            FeedbackSection(
                onFeedbackView = actions.onFeedbackView
            )
            Column {
                uiState.legalDocs.forEach { (id, title) ->
                    TextLink(title, 14, FontWeight.Normal) {
                        actions.onDocumentView(id)
                    }
                }
            }
            Divider()
            Text(
                text = AppInfo.Version.displayName,
                fontSize = 14.sp,
                color = MaterialTheme.colors.onBackground.copy(alpha = ContentAlpha.disabled),
                modifier = Modifier
                    .padding(bottom = 4.dp)
            )
            if (showDialog) {
                Dialog(onDismissRequest = { showDialog = false }) {
                    LanguagesDialog(
                        languages = uiState.languages,
                        englishMode = uiState.settings.englishMode.value
                    ) { language ->
                        actions.onSelectLanguage(language)
                        showDialog = false
                    }
                }
            }
        }
    }
}

@Composable
fun LanguagesDialog(
    languages: List<Language>,
    englishMode: Boolean,
    onSelect: (Language) -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colors.surface,
        modifier = Modifier
            .widthIn(max = 360.dp)
            .heightIn(max = 360.dp)
    ) {
        Column {
            Text(
                text = "Select test language",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(12.dp, 0.dp)
                    .padding(bottom = 12.dp)
            ) {
                Column {
                    languages.forEach { language ->
                        TextButton(
                            onClick = { onSelect(language) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            Text(
                                text = language.getText(englishMode)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeedbackSection(
    onFeedbackView: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colors.surface.copy(alpha = 0.75f)),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
        ) {
            Text(
                text = Texts.FeedbackSection.HEADER,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = Texts.FeedbackSection.BODY,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 32.dp)
            )
            Button(onFeedbackView) {
                Text(
                    text = Texts.FeedbackSection.BUTTON,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(12.dp, 6.dp)
                )
            }
        }
    }
}

@Composable
fun FeedbackView(
    message: FeedbackState.Message,
    onChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            TextField(
                value = message.value,
                onValueChange = onChange,
                label = { Text("Message") },
                shape = MaterialTheme.shapes.medium,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.25f)),
                        shape = MaterialTheme.shapes.medium
                    )
            )
        }
        LargeButton(
            text = "Send message",
            isEnabled = message.isAllowToSend
        ) {
            onSend()
        }
    }
}

@Composable
fun FeedbackSent(
    onClose: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = Texts.FeedbackSent.HEADER,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = Texts.FeedbackSent.BODY,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 32.dp)
            )
            Button(onClose) {
                Text(
                    modifier = Modifier
                        .padding(12.dp, 6.dp),
                    text = Texts.FeedbackSent.BUTTON,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun DocumentContent(
    document: DocumentState.Document
) {
    SelectionContainer {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            val defaultTextStyle = TextStyle.Default.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
            )

            document.content.forEach { line ->
                when (line) {
                    is MarkdownLine.Header -> {
                        Text(
                            text = line.text,
                            style = defaultTextStyle.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = (22 - line.level * 2).sp,
                                letterSpacing = 0.15.sp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 8.dp)
                        )
                    }
                    is MarkdownLine.ListItem -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                        ) {
                            Text(
                                text = line.marker,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(end = 6.dp)
                            )
                            Text(
                                text = buildAnnotatedString { AppendMarkdownTexts(line.segments) },
                                style = defaultTextStyle
                            )
                        }
                    }
                    is MarkdownLine.Paragraph -> {
                        Text(
                            text = buildAnnotatedString { AppendMarkdownTexts(line.segments) },
                            style = defaultTextStyle,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        )
                    }
                }
            }
        }
    }
}