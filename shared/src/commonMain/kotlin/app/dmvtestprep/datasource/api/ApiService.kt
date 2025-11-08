package app.dmvtestprep.datasource.api

import app.dmvtestprep.model.*

class ApiService(private val apiClient: ApiClient) {

    suspend fun fetchConfig(platformName: String): ConfigData {
        return apiClient.get("/config/${platformName.lowercase()}")
    }

    suspend fun fetchLanguages(): List<LanguageData> {
        return apiClient.get("/languages")
    }

    suspend fun fetchQuestions(languageCode: String): List<QuestionData> {
        return apiClient.get("/questions/$languageCode")
    }

    suspend fun fetchTest(modeName: String, languageCode: String): TestData {
        return apiClient.get("/test/${modeName.lowercase()}/$languageCode")
    }

    suspend fun sendTestResult(data: BodyData<TestResultData>): TestResultResponseData {
        return apiClient.post("/test/result", data)
    }

    suspend fun sendFeedback(data: BodyData<FeedbackData>): FeedbackResponseData {
        return apiClient.post("/feedback", data)
    }

    suspend fun fetchDocument(id: Int): DocumentData {
        return apiClient.get("/document/$id")
    }
}