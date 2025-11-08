package app.dmvtestprep.domain.model

import app.dmvtestprep.domain.model.TestMode.*
import app.dmvtestprep.domain.settings.Setting
import app.dmvtestprep.domain.settings.ModeSettings
import app.dmvtestprep.domain.settings.TestSettingType

data class TestConfigState(
    private val learnedQuestions: Int,
    private val prepSettings: ModeSettings,
    private val examSettings: ModeSettings,
    val testMode: TestMode,
    val showTest: Boolean
) {
    private val modeSettings: ModeSettings
        get() = when (testMode) {
            PREP -> prepSettings
            EXAM -> examSettings
        }

    val title: String = "Test settings"
    val skipLearnedQuestions: Setting<Boolean>
        get() = modeSettings.skipLearnedQuestions
    val showCorrectAnswer: Setting<Boolean>
        get() = modeSettings.showCorrectAnswer
    val shuffleQuestions: Setting<Boolean>
        get() = modeSettings.shuffleQuestions
    val shuffleAnswers: Setting<Boolean>
        get() = modeSettings.shuffleAnswers
    val totalQuestions: Setting<Int>
        get() = Setting(
            label = modeSettings.totalQuestions.label,
            value = modeSettings.totalQuestions.value.let {
                if (skipLearnedQuestions.value) maxOf(0, it - learnedQuestions) else it
            },
            isEnabled = modeSettings.totalQuestions.isEnabled
        )
    val maxErrors: Setting<Int>
        get() = Setting(
            label = modeSettings.maxErrors.label,
            value = modeSettings.maxErrors.value.coerceIn(0, totalQuestions.value),
            isEnabled = modeSettings.maxErrors.isEnabled && totalQuestions.value > 0
        )
    val startButton: Setting<String>
        get() = Setting(
            label = "Start test",
            value = when (testMode) {
                PREP -> "Start prep"
                EXAM -> "Start exam"
            },
            isEnabled = totalQuestions.value > 0
        )

    fun update(type: TestSettingType): TestConfigState {
        return when (testMode) {
            PREP -> copy(prepSettings = prepSettings.update(type))
            EXAM -> copy(examSettings = examSettings.update(type))
        }
    }
}