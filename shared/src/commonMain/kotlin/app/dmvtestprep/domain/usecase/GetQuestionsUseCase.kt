package app.dmvtestprep.domain.usecase

import app.dmvtestprep.domain.model.Question
import app.dmvtestprep.domain.settings.DefaultSetting
import app.dmvtestprep.repository.RemoteRepository
import app.dmvtestprep.repository.SettingsRepository

class GetQuestionsUseCase(
    private val remoteRepository: RemoteRepository,
    private val settingsRepository: SettingsRepository,
    private val prepareQuestionsUseCase: PrepareQuestionsUseCase
) {

    private var cachedLanguageCode: String? = null
    private var cachedQuestions: List<Question>? = null

    suspend operator fun invoke(ignoreCache: Boolean): List<Question> {
        val languageCode = settingsRepository.getSettingValue(DefaultSetting.LanguageCode)
        val answerPrefix = settingsRepository.getSettingValue(DefaultSetting.AnswerPrefix)
        val takeCache = !ignoreCache && cachedLanguageCode == languageCode
        val questions = cachedQuestions.takeIf { takeCache } ?: fetchQuestions(languageCode)

        return prepareQuestionsUseCase.prepare(
            questions = questions,
            setLearningStage = true,
            skipLearnedQuestions = false,
            shuffleQuestions = false,
            shuffleAnswers = false,
            answerPrefix = answerPrefix
        )
    }

    private suspend fun fetchQuestions(languageCode: String): List<Question> {
        return remoteRepository.getQuestions(languageCode).also {
            cachedLanguageCode = languageCode
            cachedQuestions = it
        }
    }

}