package app.dmvtestprep.ui.screen

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
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dmvtestprep.asColor
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.dmvtestprep.domain.model.StatisticsSection
import app.dmvtestprep.domain.model.StatisticsState
import app.dmvtestprep.domain.usecase.UseCaseProvider
import app.dmvtestprep.ui.common.ConfirmAlertDialog
import app.dmvtestprep.ui.common.ErrorView
import app.dmvtestprep.ui.common.LargeButton
import app.dmvtestprep.viewmodel.StatisticsViewModel

@Composable
fun StatisticsScreen() {
    val viewModel = remember {
        StatisticsViewModel(
            UseCaseProvider.statisticsFactory,
            UseCaseProvider.clearStatisticsUseCase
        )
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showClearConfirmation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadStatistics()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clear()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = uiState.title)
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
                    is StatisticsState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    is StatisticsState.Statistics -> {
                        StatisticsView(
                            sections = state.sections
                        ) {
                            showClearConfirmation = true
                        }
                    }
                    is StatisticsState.Error -> {
                        ErrorView(
                            errorSummary = state.errorSummary,
                            onRetry = state.onRetry
                        )
                    }
                }
            }
        }
    )
    if (showClearConfirmation) {
        ConfirmAlertDialog(
            text = "Are you sure you want to clear all statistics?",
            confirmButtonText = "Clear",
            onDismiss = { showClearConfirmation = false }
        ) {
            showClearConfirmation = false
            viewModel.onClearStatistics()
            viewModel.loadStatistics()
        }
    }
}

@Composable
fun StatisticsView(
    sections: List<StatisticsSection>,
    onClearStatistics: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp, 20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            sections.forEach { section ->
                StatisticsSectionView(
                    header = section.header,
                    rows = section.rows
                )
            }
            LargeButton(
                text = "Clear statistics"
            ) {
                onClearStatistics()
            }
        }
    }
}

@Composable
fun StatisticsSectionView(
    header: String,
    rows: List<StatisticsSection.StatisticRow>
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colors.surface.copy(alpha = 0.75f)),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .padding(12.dp)
                .padding(bottom = 6.dp)
        ) {
            Text(
                text = header,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Divider(thickness = 1.dp, modifier = Modifier.padding(vertical = 6.dp))
            rows.forEach { row ->
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(text = row.label)
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .height(1.dp)
                        .padding(horizontal = 4.dp)
                        .border(width = 1.dp, color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
                    )
                    Text(
                        text = row.value,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = row.color.asColor()
                    )
                }
            }
        }
    }
}