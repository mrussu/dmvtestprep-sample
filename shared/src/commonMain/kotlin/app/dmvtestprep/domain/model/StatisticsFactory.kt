package app.dmvtestprep.domain.model

import app.dmvtestprep.repository.DatabaseRepository

class StatisticsFactory(private val databaseRepository: DatabaseRepository) {

    fun getStatistics(): List<StatisticsSection> {
        return listOf(
            getPerformanceStatistics(),
            getQuestionsStatistics(),
            getTestStatistics()
        )
    }

    private fun getPerformanceStatistics(): StatisticsSection {
        val rawStatistics = databaseRepository.getPerformanceStatistics()

        return StatisticsSection(
            header = StatisticType.Performance.HEADER,
            rows = listOf(
                createStatisticRow(StatisticType.Performance.AnswerAccuracy, rawStatistics.answer_accuracy),
                createStatisticRow(StatisticType.Performance.AvgAnswerTime, rawStatistics.avg_answer_time),
                createStatisticRow(StatisticType.Performance.TotalAnswerTime, rawStatistics.total_answer_time)
            )
        )
    }

    private fun getQuestionsStatistics(): StatisticsSection {
        val rawStatistics = databaseRepository.getQuestionsStatistics()

        return StatisticsSection(
            header = StatisticType.Questions.HEADER,
            rows = listOf(
                createStatisticRow(StatisticType.Questions.CorrectAnswers, rawStatistics.correct_answers.toInt()),
                createStatisticRow(StatisticType.Questions.IncorrectAnswers, rawStatistics.incorrect_answers.toInt()),
                createStatisticRow(StatisticType.Questions.TotalAnswered, rawStatistics.total_answered.toInt())
            )
        )
    }

    private fun getTestStatistics(): StatisticsSection {
        val rawStatistics = databaseRepository.getTestStatistics()

        return StatisticsSection(
            header = StatisticType.Tests.HEADER,
            rows = listOf(
                createStatisticRow(StatisticType.Tests.Completed, rawStatistics.completed.toInt()),
                createStatisticRow(StatisticType.Tests.Passed, rawStatistics.passed.toInt()),
                createStatisticRow(StatisticType.Tests.Failed, rawStatistics.failed.toInt()),
                createStatisticRow(StatisticType.Tests.Abandoned, rawStatistics.abandoned.toInt()),
                createStatisticRow(StatisticType.Tests.TotalAttempts, rawStatistics.total_attempts.toInt())
            )
        )
    }

    private fun <T : Any> createStatisticRow(statisticType: StatisticType<T>, databaseValue: T): StatisticsSection.StatisticRow {
        return StatisticsSection.StatisticRow(
            label = statisticType.label,
            value = statisticType.formatValue(databaseValue),
            color = statisticType.getColor(databaseValue)
        )
    }
}