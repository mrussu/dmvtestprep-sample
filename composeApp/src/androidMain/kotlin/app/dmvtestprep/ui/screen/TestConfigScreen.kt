package app.dmvtestprep.ui.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import app.dmvtestprep.domain.model.TestConfigState
import app.dmvtestprep.domain.model.TestMode
import app.dmvtestprep.domain.model.TestMode.*
import app.dmvtestprep.domain.resources.MaxErrorsResources
import app.dmvtestprep.domain.usecase.UseCaseProvider
import app.dmvtestprep.ui.common.LargeButton
import app.dmvtestprep.ui.common.SegmentedButton
import app.dmvtestprep.ui.common.SettingToggle
import app.dmvtestprep.ui.icons.Icons
import app.dmvtestprep.viewmodel.TestConfigViewModel
import app.dmvtestprep.viewmodel.actions.TestConfigActionsHandler
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun TestConfigScreen(
    onBottomNavVisibilityChanged: (Boolean) -> Unit,
    onSettingsClick: () -> Unit
) {
    val viewModel = remember {
        TestConfigViewModel(
            UseCaseProvider.learnedQuestionsUseCase,
            UseCaseProvider.testSettingsUseCase,
            UseCaseProvider.settingFactory
        )
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = TestConfigActionsHandler(viewModel) { viewModel.startTest() }

    LaunchedEffect(uiState.showTest) {
        onBottomNavVisibilityChanged(!uiState.showTest)
    }

    LaunchedEffect(Unit) {
        viewModel.updateLearnedQuestions()
    }

    Crossfade(targetState = uiState.showTest, label = "TestScreenTransition") { isTestScreenVisible ->
        if (isTestScreenVisible) {
            TestScreen(onDismiss = { viewModel.finishTest() })
        } else {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = uiState.title)
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    onSettingsClick()
                                },
                            ) {
                                Icon(
                                    painter = Icons.Settings,
                                    contentDescription = "Settings"
                                )
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
                        TestConfigContent(
                            uiState = uiState,
                            actions = actions
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun TestConfigContent(
    uiState: TestConfigState,
    actions: TestConfigActionsHandler
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp, 20.dp)
    ) {
        TestModePicker(
            selectedMode = uiState.testMode,
            onTestModeChanged = { testMode ->
                actions.onTestModeChanged(testMode)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = when (uiState.testMode) {
                PREP -> PREP.modeText
                EXAM -> EXAM.modeText
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.onBackground.copy(alpha = ContentAlpha.disabled),
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Divider(modifier = Modifier.padding(vertical = 10.dp))
        MaxErrorsSliderView(
            label = uiState.maxErrors.label,
            value = uiState.maxErrors.value,
            total = uiState.totalQuestions.value,
            isEnabled = uiState.maxErrors.isEnabled
        ) {
            actions.onMaxErrorsChanged(it)
        }
        SettingToggle(
            setting = uiState.skipLearnedQuestions,
            onToggle = { actions.onSkipLearnedQuestionsChanged(it) }
        )
        Divider(modifier = Modifier.padding(vertical = 10.dp))
        SettingToggle(
            setting = uiState.showCorrectAnswer,
            onToggle = { actions.onShowCorrectAnswerChanged(it) }
        )
        SettingToggle(
            setting = uiState.shuffleQuestions,
            onToggle = { actions.onShuffleQuestionsChanged(it) }
        )
        SettingToggle(
            setting = uiState.shuffleAnswers,
            onToggle = { actions.onShuffleAnswersChanged(it) }
        )
        Spacer(modifier = Modifier.height(20.dp))
        LargeButton(
            text = uiState.startButton.value,
            isEnabled = uiState.startButton.isEnabled
        ) {
            actions.onStartTest()
        }
    }
}

@Composable
fun TestModePicker(
    selectedMode: TestMode,
    onTestModeChanged: (TestMode) -> Unit
) {
    SegmentedButton(
        options = entries,
        selectedOption = selectedMode,
        onOptionSelected = onTestModeChanged,
        getLabel = { it.modeName }
    )
}

@Composable
fun MaxErrorsSliderView(
    label: String,
    total: Int,
    value: Int,
    isEnabled: Boolean,
    onValueChange: (Int) -> Unit
) {
    val step = 1
    val range = 0f..max(1f, total.toFloat())
    val progress = range.run { (value - start) / (endInclusive - start) }.toDouble()
    val dynamicText = MaxErrorsResources.getText(progress)
    val dynamicColor = MaxErrorsResources.getColor(progress).asColor()

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp)
        ) {
            Text(
                text = label,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$value out of $total",
                textAlign = TextAlign.Right,
                maxLines = 1
            )
        }
        Text(
            text = dynamicText,
            style = MaterialTheme.typography.caption,
            color = dynamicColor,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Slider(
            value = value.toFloat(),
            onValueChange = { newValue ->
                val steppedValue = (newValue / step).roundToInt() * step
                if (isEnabled) onValueChange(steppedValue)
            },
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = dynamicColor,
                activeTrackColor = dynamicColor
            ),
            enabled = isEnabled,
            modifier = Modifier
                .height(48.dp)
        )
    }
}