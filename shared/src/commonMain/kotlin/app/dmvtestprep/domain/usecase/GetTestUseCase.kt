package app.dmvtestprep.domain.usecase

import app.dmvtestprep.core.config.AppInfo
import app.dmvtestprep.domain.model.Test
import app.dmvtestprep.domain.model.TestConfig
import app.dmvtestprep.domain.model.TestMode
import app.dmvtestprep.domain.model.TestResult
import app.dmvtestprep.domain.model.TestResultResponse
import app.dmvtestprep.repository.RemoteRepository
import app.dmvtestprep.repository.SettingsRepository

class GetTestUseCase(
    private val remoteRepository: RemoteRepository,
    private val settingsRepository: SettingsRepository,
    private val prepareQuestionsUseCase: PrepareQuestionsUseCase
) {

    private var cachedPrepTest: Test? = null
    private var cachedPrepConfig: TestConfig? = null

    suspend operator fun invoke(): Test {
        val settings = settingsRepository.getSettings()
        val testConfig = settingsRepository.getTestConfig()
        val testSettings = settingsRepository.getTestSettings(testConfig.modeName)
        val takeCache = testConfig == cachedPrepConfig
        val test = cachedPrepTest.takeIf { takeCache } ?: fetchTest(testConfig)
        val questions = prepareQuestionsUseCase.prepare(
            questions = test.questions,
            setLearningStage = false,
            skipLearnedQuestions = testSettings.skipLearnedQuestions,
            shuffleQuestions = testSettings.shuffleQuestions,
            shuffleAnswers = testSettings.shuffleAnswers,
            answerPrefix = settings.answerPrefix
        )

        return test.copy(
            questions = questions,
            settings = testSettings
        )
    }

    suspend fun sendTestResult(testResult: TestResult): TestResultResponse {
        return if (AppInfo.buildContext.isDebug) TestResultResponse.Failure
        else remoteRepository.sendTestResult(testResult, AppInfo.usageContext)
    }

    private suspend fun fetchTest(testConfig: TestConfig): Test {
        return remoteRepository.getTest(testConfig).also {
            if (testConfig.modeName == TestMode.PREP.modeName) {
                cachedPrepTest = it
                cachedPrepConfig = testConfig
            }
        }
    }

}