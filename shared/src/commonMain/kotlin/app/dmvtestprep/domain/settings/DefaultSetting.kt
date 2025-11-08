package app.dmvtestprep.domain.settings

import app.dmvtestprep.domain.model.TestMode
import app.dmvtestprep.domain.model.Language
import app.dmvtestprep.domain.model.NavTab

sealed class DefaultSetting<T>(val key: String, val defaultValue: T) {
    open class IntSetting(key: String, defaultValue: Int) : DefaultSetting<Int>(key, defaultValue)
    open class StringSetting(key: String, defaultValue: String) : DefaultSetting<String>(key, defaultValue)
    open class BooleanSetting(key: String, defaultValue: Boolean) : DefaultSetting<Boolean>(key, defaultValue)

    object NavTabId : IntSetting("selected_tab", NavTab.DEFAULT_TAB.id)
    object LanguageCode : StringSetting("language_code", Language.DEFAULT_LANGUAGE_CODE)

    object EnglishMode : BooleanSetting("english_mode", false)
    object AnswerPrefix : BooleanSetting("answer_prefix", false)

    object TestModeName : StringSetting("test_mode_name", TestMode.PREP.modeName)

    object PrepSkipLearnedQuestions : BooleanSetting("prep_skip_learned_questions", false)
    object PrepShowCorrectAnswer : BooleanSetting("prep_show_correct_answer", true)
    object PrepShuffleQuestions : BooleanSetting("prep_shuffle_questions", false)
    object PrepShuffleAnswers : BooleanSetting("prep_shuffle_answers", false)
    object PrepTotalQuestions : IntSetting("prep_total_questions", 182)
    object PrepMaxErrors : IntSetting("prep_max_errors", 50)

    object ExamSkipLearnedQuestions : BooleanSetting("exam_skip_learned_questions", false)
    object ExamShowCorrectAnswer : BooleanSetting("exam_show_correct_answer", false)
    object ExamShuffleQuestions : BooleanSetting("exam_shuffle_questions", false)
    object ExamShuffleAnswers : BooleanSetting("exam_shuffle_answers", false)
    object ExamTotalQuestions : IntSetting("exam_total_questions", 18)
    object ExamMaxErrors : IntSetting("exam_max_errors", 3)
}