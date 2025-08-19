package app.dmvtestprep.domain.model

import app.dmvtestprep.domain.utils.TimeUtils

sealed class StatisticType<T>(
    val label: String
) {
    private val defaultColor = ColorModel.black.copy(a = 0.5f)

    open fun formatValue(value: T): String = value.toString()
    open fun getColor(value: T): ColorModel = defaultColor

    internal fun determineColor(value: Int, color: ColorModel): ColorModel {
        return if (value == 0) defaultColor else color
    }

    object Performance {
        const val HEADER: String = Labels.PERFORMANCE

        data object AnswerAccuracy : StatisticType<Double>(
            label = Labels.ANSWER_ACCURACY
        ) {
            override fun formatValue(value: Double): String {
                return "${(value.coerceIn(0.0, 100.0) * 100).toInt() / 100.0}%"
            }

            override fun getColor(value: Double): ColorModel {
                return when (value.compareTo(-1.0)) {
                    0 -> super.getColor(value)
                    else -> ColorModel.getBlendColor(
                        factor = value.coerceIn(0.0, 100.0),
                        colors = listOf(
                            ColorModel.brightRed,
                            ColorModel.brightOrange,
                            ColorModel.brightGreen
                        ),
                        isPercent = true
                    )
                }
            }
        }

        data object AvgAnswerTime : StatisticType<Long>(
            label = Labels.AVG_ANSWER_TIME
        ) {
            override fun formatValue(value: Long): String = TimeUtils.formatDuration(value)
        }

        data object TotalAnswerTime : StatisticType<Long>(
            label = Labels.TOTAL_ANSWER_TIME
        ) {
            override fun formatValue(value: Long): String = TimeUtils.formatDuration(value)
        }
    }

    object Questions {
        const val HEADER: String = Labels.QUESTIONS

        data object CorrectAnswers : StatisticType<Int>(
            label = Labels.CORRECT_ANSWERS
        ) {
            override fun getColor(value: Int) = determineColor(value, ColorModel.brightGreen)
        }

        data object IncorrectAnswers : StatisticType<Int>(
            label = Labels.INCORRECT_ANSWERS
        ) {
            override fun getColor(value: Int) = determineColor(value, ColorModel.brightRed)
        }

        data object TotalAnswered : StatisticType<Int>(
            label = Labels.TOTAL_QUESTIONS_ANSWERED
        )
    }

    object Tests {
        const val HEADER: String = Labels.TESTS

        data object Completed : StatisticType<Int>(
            label = Labels.COMPLETED
        )

        data object Passed : StatisticType<Int>(
            label = Labels.PASSED
        ) {
            override fun getColor(value: Int) = determineColor(value, ColorModel.brightGreen)
        }

        data object Failed : StatisticType<Int>(
            label = Labels.FAILED
        ) {
            override fun getColor(value: Int) = determineColor(value, ColorModel.brightRed)
        }

        data object Abandoned : StatisticType<Int>(
            label = Labels.ABANDONED
        )

        data object TotalAttempts : StatisticType<Int>(
            label = Labels.TOTAL_ATTEMPTS
        )
    }

    companion object {
        object Labels {
            const val TESTS = "Tests"
            const val COMPLETED = "Completed"
            const val PASSED = "Passed"
            const val FAILED = "Failed"
            const val ABANDONED = "Abandoned"
            const val TOTAL_ATTEMPTS = "Total attempts"

            const val QUESTIONS = "Questions"
            const val CORRECT_ANSWERS = "Correct answers"
            const val INCORRECT_ANSWERS = "Incorrect answers"
            const val TOTAL_QUESTIONS_ANSWERED = "Total questions answered"

            const val PERFORMANCE = "Performance"
            const val ANSWER_ACCURACY = "Answer accuracy"
            const val AVG_ANSWER_TIME = "Average answer time"
            const val TOTAL_ANSWER_TIME = "Total answer time"
        }
    }
}