package app.dmvtestprep.repository

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

interface ApiRepository {
    suspend fun getConfig(platformName: String): Config
    suspend fun getLanguages(): List<Language>
    suspend fun getQuestions(languageCode: String): List<Question>
    suspend fun getTest(testConfig: TestConfig): Test
    suspend fun sendTestResult(testResult: TestResult, usageContext: UsageContext): TestResultResponse
    suspend fun sendFeedback(feedback: Feedback, usageContext: UsageContext): FeedbackResponse
    suspend fun getDocument(id: Int): Document
}