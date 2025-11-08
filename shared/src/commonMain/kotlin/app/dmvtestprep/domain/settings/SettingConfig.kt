package app.dmvtestprep.domain.settings

sealed class SettingConfig<T>(
    val label: String,
    val details: String,
    val isEnabled: Boolean,
    val defaultSetting: DefaultSetting<T>
) {

    data object EnglishMode : SettingConfig<Boolean>(
        label = Labels.ENGLISH_MODE,
        details = Details.ENGLISH_MODE,
        isEnabled = IsEnabled.ENGLISH_MODE,
        defaultSetting = DefaultSetting.EnglishMode
    )

    data object AnswerPrefix : SettingConfig<Boolean>(
        label = Labels.ANSWER_PREFIX,
        details = Details.ANSWER_PREFIX,
        isEnabled = IsEnabled.ANSWER_PREFIX,
        defaultSetting = DefaultSetting.AnswerPrefix
    )

    object Prep {
        data object SkipLearnedQuestions : SettingConfig<Boolean>(
            label = Labels.SKIP_LEARNED_QUESTIONS,
            details = Details.SKIP_LEARNED_QUESTIONS,
            isEnabled = IsEnabled.Prep.SKIP_LEARNED_QUESTIONS,
            defaultSetting = DefaultSetting.PrepSkipLearnedQuestions
        )

        data object ShowCorrectAnswer : SettingConfig<Boolean>(
            label = Labels.SHOW_CORRECT_ANSWER,
            details = Details.SHOW_CORRECT_ANSWER,
            isEnabled = IsEnabled.Prep.SHOW_CORRECT_ANSWER,
            defaultSetting = DefaultSetting.PrepShowCorrectAnswer
        )

        data object ShuffleQuestions : SettingConfig<Boolean>(
            label = Labels.SHUFFLE_QUESTIONS,
            details = Details.SHUFFLE_QUESTIONS,
            isEnabled = IsEnabled.Prep.SHUFFLE_QUESTIONS,
            defaultSetting = DefaultSetting.PrepShuffleQuestions
        )

        data object ShuffleAnswers : SettingConfig<Boolean>(
            label = Labels.SHUFFLE_ANSWERS,
            details = Details.SHUFFLE_ANSWERS,
            isEnabled = IsEnabled.Prep.SHUFFLE_ANSWERS,
            defaultSetting = DefaultSetting.PrepShuffleAnswers
        )

        data object TotalQuestions : SettingConfig<Int>(
            label = Labels.TOTAL_QUESTIONS,
            details = Details.TOTAL_QUESTIONS,
            isEnabled = IsEnabled.Prep.TOTAL_QUESTIONS,
            defaultSetting = DefaultSetting.PrepTotalQuestions
        )

        data object MaxErrors : SettingConfig<Int>(
            label = Labels.MAX_ERRORS,
            details = Details.MAX_ERRORS,
            isEnabled = IsEnabled.Prep.MAX_ERRORS,
            defaultSetting = DefaultSetting.PrepMaxErrors
        )
    }

    object Exam {
        data object SkipLearnedQuestions : SettingConfig<Boolean>(
            label = Labels.SKIP_LEARNED_QUESTIONS,
            details = Details.SKIP_LEARNED_QUESTIONS,
            isEnabled = IsEnabled.Exam.SKIP_LEARNED_QUESTIONS,
            defaultSetting = DefaultSetting.ExamSkipLearnedQuestions
        )

        data object ShowCorrectAnswer : SettingConfig<Boolean>(
            label = Labels.SHOW_CORRECT_ANSWER,
            details = Details.SHOW_CORRECT_ANSWER,
            isEnabled = IsEnabled.Exam.SHOW_CORRECT_ANSWER,
            defaultSetting = DefaultSetting.ExamShowCorrectAnswer
        )

        data object ShuffleQuestions : SettingConfig<Boolean>(
            label = Labels.SHUFFLE_QUESTIONS,
            details = Details.SHUFFLE_QUESTIONS,
            isEnabled = IsEnabled.Exam.SHUFFLE_QUESTIONS,
            defaultSetting = DefaultSetting.ExamShuffleQuestions
        )

        data object ShuffleAnswers : SettingConfig<Boolean>(
            label = Labels.SHUFFLE_ANSWERS,
            details = Details.SHUFFLE_ANSWERS,
            isEnabled = IsEnabled.Exam.SHUFFLE_ANSWERS,
            defaultSetting = DefaultSetting.ExamShuffleAnswers
        )

        data object TotalQuestions : SettingConfig<Int>(
            label = Labels.TOTAL_QUESTIONS,
            details = Details.TOTAL_QUESTIONS,
            isEnabled = IsEnabled.Exam.TOTAL_QUESTIONS,
            defaultSetting = DefaultSetting.ExamTotalQuestions
        )

        data object MaxErrors : SettingConfig<Int>(
            label = Labels.MAX_ERRORS,
            details = Details.MAX_ERRORS,
            isEnabled = IsEnabled.Exam.MAX_ERRORS,
            defaultSetting = DefaultSetting.ExamMaxErrors
        )
    }

    companion object {
        object Labels {
            const val ENGLISH_MODE = "English mode"
            const val ANSWER_PREFIX = "Answer prefix"
            const val SKIP_LEARNED_QUESTIONS = "Skip learned questions"
            const val SHOW_CORRECT_ANSWER = "Show correct answer"
            const val SHUFFLE_QUESTIONS = "Shuffle questions"
            const val SHUFFLE_ANSWERS = "Shuffle answers"
            const val TOTAL_QUESTIONS = "Total questions"
            const val MAX_ERRORS = "Maximum errors to pass"
        }

        object Details {
            const val ENGLISH_MODE = "Switch to English text to view the original content, with the option to toggle this mode while preparing for the driving test."
            const val ANSWER_PREFIX = "Display A, B, C, D for answers, or 1, 2, 3, 4 when shuffle is enabled to avoid confusion."
            const val SKIP_LEARNED_QUESTIONS = ""
            const val SHOW_CORRECT_ANSWER = ""
            const val SHUFFLE_QUESTIONS = ""
            const val SHUFFLE_ANSWERS = ""
            const val TOTAL_QUESTIONS = ""
            const val MAX_ERRORS = ""
        }

        object IsEnabled {
            const val ENGLISH_MODE = true
            const val ANSWER_PREFIX = true

            object Prep {
                const val SKIP_LEARNED_QUESTIONS = true
                const val SHOW_CORRECT_ANSWER = true
                const val SHUFFLE_QUESTIONS = true
                const val SHUFFLE_ANSWERS = true
                const val TOTAL_QUESTIONS = true
                const val MAX_ERRORS = true
            }

            object Exam {
                const val SKIP_LEARNED_QUESTIONS = false
                const val SHOW_CORRECT_ANSWER = true
                const val SHUFFLE_QUESTIONS = false // Order of questions is handled by the API
                const val SHUFFLE_ANSWERS = true
                const val TOTAL_QUESTIONS = false
                const val MAX_ERRORS = false // Max errors is handled by the API
            }
        }
    }
}