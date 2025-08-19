package app.dmvtestprep.domain.model

data class StatisticsSection(
    val header: String,
    val rows: List<StatisticRow>
) {
    data class StatisticRow(
        val label: String,
        val value: String,
        val color: ColorModel
    )
}