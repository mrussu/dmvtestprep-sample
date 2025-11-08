package app.dmvtestprep.domain.model

import app.dmvtestprep.core.ui.model.ThemeColor

data class StatisticsSection(
    val header: String,
    val rows: List<StatisticRow>
) {
    data class StatisticRow(
        val label: String,
        val value: String,
        val color: ThemeColor
    )
}