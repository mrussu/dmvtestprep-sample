package app.dmvtestprep.domain.model

import app.dmvtestprep.core.ui.model.ThemeColor
import app.dmvtestprep.core.ui.theme.AppColors

data class LearningStage(
    val type: Type = Type.EMPTY,
    val isUpToDate: Boolean = false
) {
    enum class Type(val value: String, val trend: Trend) {
        LEARNED("Learned", Trend.Positive),
        CONFIDENT("Confident", Trend.Positive),
        FAMILIAR("Familiar", Trend.Positive),
        GROWING("Growing", Trend.Positive),
        IMPROVING("Improving", Trend.Positive),
        PROGRESSING("Progressing", Trend.Positive),
        TRAILING("Trailing", Trend.Positive),
        SLIPPING("Slipping", Trend.Negative),
        FORGETTING("Forgetting", Trend.Negative),
        REGRESSING("Regressing", Trend.Negative),
        UNCERTAIN("Uncertain", Trend.Negative),
        CHALLENGING("Challenging", Trend.Negative),
        DIFFICULT("Difficult", Trend.Negative),
        IMPOSSIBLE("Impossible", Trend.Negative),
        UNKNOWN("Unknown", Trend.Undefined),
        EMPTY("", Trend.Undefined);

        fun isLearning(): Boolean = this !in listOf(LEARNED, EMPTY)
        fun isLearned(): Boolean = this == LEARNED
        fun createStage(isUpToDate: Boolean): LearningStage = LearningStage(this, isUpToDate)

        companion object {
            private val sequences = mapOf(
                "111" to LEARNED,
                "11" to CONFIDENT,
                "1" to FAMILIAR,
                "10" to GROWING,
                "110" to IMPROVING,
                "100" to PROGRESSING,
                "101" to TRAILING,
                "01" to SLIPPING,
                "011" to FORGETTING,
                "001" to REGRESSING,
                "010" to UNCERTAIN,
                "0" to CHALLENGING,
                "00" to DIFFICULT,
                "000" to IMPOSSIBLE,
                "" to EMPTY
            )
            fun getType(sequenceOfAnswers: String) = sequences[sequenceOfAnswers] ?: UNKNOWN
        }
    }

    sealed class Trend {
        data object Positive: Trend()
        data object Negative: Trend()
        data object Undefined: Trend()

        fun getTextColor(isUpToDate: Boolean): ThemeColor {
            val alpha = if (isUpToDate) 0.95f else 0.65f

            return when (this) {
                Positive -> AppColors.onPrimary.copy(alpha = alpha)
                Negative -> AppColors.onPrimary.copy(alpha = alpha)
                Undefined -> AppColors.onBackground.copy(alpha = alpha)
            }
        }

        fun getBackgroundColor(isUpToDate: Boolean): ThemeColor {
            val alpha = if (isUpToDate) 0.85f else 0.45f

            return when (this) {
                Positive -> AppColors.success.copy(alpha = alpha)
                Negative -> AppColors.warning.copy(alpha = alpha)
                Undefined -> AppColors.background
            }
        }
    }

    fun getTextColor(): ThemeColor {
        return type.trend.getTextColor(isUpToDate)
    }

    fun getBackgroundColor(): ThemeColor {
        return type.trend.getBackgroundColor(isUpToDate)
    }

    companion object {
        fun determineStage(sequenceOfAnswers: String, isUpToDate: Boolean): LearningStage {
            val type = Type.getType(sequenceOfAnswers)
            return type.createStage(isUpToDate)
        }
    }
}