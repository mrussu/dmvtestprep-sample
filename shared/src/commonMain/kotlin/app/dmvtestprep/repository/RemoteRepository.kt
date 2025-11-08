package app.dmvtestprep.repository

import app.dmvtestprep.datasource.api.ApiService
import app.dmvtestprep.domain.model.Config
import app.dmvtestprep.domain.model.DocumentState.Document
import app.dmvtestprep.domain.model.Feedback
import app.dmvtestprep.domain.model.FeedbackResponse
import app.dmvtestprep.domain.model.Language
import app.dmvtestprep.domain.model.Question
import app.dmvtestprep.domain.model.Test
import app.dmvtestprep.domain.model.TestConfig
import app.dmvtestprep.domain.model.TestResult
import app.dmvtestprep.domain.model.TestResultResponse
import app.dmvtestprep.domain.model.UsageContext
import app.dmvtestprep.domain.model.toDocument
import app.dmvtestprep.domain.model.toFeedbackResponse
import app.dmvtestprep.domain.model.toLanguages
import app.dmvtestprep.domain.model.toBodyData
import app.dmvtestprep.domain.model.toConfig
import app.dmvtestprep.domain.model.toQuestion
import app.dmvtestprep.domain.model.toTest
import app.dmvtestprep.domain.model.toTestResultResponse

class RemoteRepository(private val apiService: ApiService) : ApiRepository {

    override suspend fun getConfig(platformName: String): Config {
        return apiService.fetchConfig(platformName).toConfig()
    }

    override suspend fun getLanguages(): List<Language> {
        return apiService.fetchLanguages().toLanguages()
    }

    override suspend fun getQuestions(languageCode: String): List<Question> {
        return apiService.fetchQuestions(languageCode).map { it.toQuestion() }
    }

    override suspend fun getTest(testConfig: TestConfig): Test {
        return apiService.fetchTest(testConfig.modeName, testConfig.languageCode).toTest()
    }

    override suspend fun sendTestResult(testResult: TestResult, usageContext: UsageContext): TestResultResponse {
        return apiService.sendTestResult(testResult.toBodyData(usageContext)).toTestResultResponse()
    }

    override suspend fun sendFeedback(feedback: Feedback, usageContext: UsageContext): FeedbackResponse {
        return apiService.sendFeedback(feedback.toBodyData(usageContext)).toFeedbackResponse()
    }

    override suspend fun getDocument(id: Int): Document {
        return apiService.fetchDocument(id).toDocument()
    }

}