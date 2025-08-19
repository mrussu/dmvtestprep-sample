package app.dmvtestprep.datasource.storage

import app.cash.sqldelight.db.SqlDriver
import app.dmvtestprep.domain.model.TestResult
import app.dmvtestprep.domain.model.UserAnswer
import app.dmvtestprep.shared.db.AppDatabase

interface DatabaseStorage {

    suspend fun startNewTest(modeName: String, languageCode: String, totalQuestions: Int, maxErrors: Int): Int
    suspend fun completeTest(id: Int, correctAnswers: Int, incorrectAnswers: Int, isPassed: Boolean)

    suspend fun saveUserAnswer(userAnswer: UserAnswer)

    suspend fun getTestResult(id: Int): TestResult?
    suspend fun getUserAnswersForTest(testId: Int): List<UserAnswer>
    suspend fun getUserAnswersForQuestion(questionId: Int): List<UserAnswer>

}

expect fun provideSqlDriver(): SqlDriver

fun createDatabase(): AppDatabase {
    val driver = provideSqlDriver()
    return AppDatabase(driver)
}