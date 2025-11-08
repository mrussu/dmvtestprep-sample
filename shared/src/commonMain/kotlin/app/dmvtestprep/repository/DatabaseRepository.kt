package app.dmvtestprep.repository

import app.dmvtestprep.core.utils.TimeUtils
import app.dmvtestprep.datasource.storage.DatabaseStorage
import app.dmvtestprep.db.PerformanceStatistics
import app.dmvtestprep.db.QuestionsStatistics
import app.dmvtestprep.db.TestStatistics
import app.dmvtestprep.db.User_answers
import app.dmvtestprep.domain.model.TestResult
import app.dmvtestprep.domain.model.UserAnswer
import app.dmvtestprep.shared.db.AppDatabase

class DatabaseRepository(private val database: AppDatabase) : DatabaseStorage {

    fun clearStatistics() {
        database.commonQueries.clearStatistics()
    }

    fun getPerformanceStatistics(): PerformanceStatistics {
        return database.userAnswersQueries.performanceStatistics()
            .executeAsOne()
    }

    fun getQuestionsStatistics(): QuestionsStatistics {
        return database.userAnswersQueries.questionsStatistics()
            .executeAsOne()
    }

    fun getTestStatistics(): TestStatistics {
        return database.testResultsQueries.testStatistics()
            .executeAsOne()
    }

    fun getLearnedQuestionsCount(): Int {
        return database.learnedQuestionsQueries.count()
            .executeAsOne()
            .toInt()
    }

    fun getLearnedQuestionsIds(): Set<Int> {
        return database.learnedQuestionsQueries.selectAll()
            .executeAsList()
            .mapTo(HashSet(), Long::toInt)
    }

    fun updateLearnedQuestions(questionId: Int, isCorrect: Boolean) {
        database.learnedQuestionsQueries.run {
            questionId.toLong().let {
                if (isCorrect) insertIfLearned(it) else delete(it)
            }
        }
    }

    fun toggleSavedQuestion(questionId: Int): Boolean {
        val questionIdLong = questionId.toLong()

        return database.transactionWithResult {
            database.savedQuestionsQueries.run {
                when (selectByQuestionId(questionIdLong).executeAsOneOrNull()) {
                    null -> insertQuestionId(questionIdLong).let { true }
                    else -> deleteQuestionId(questionIdLong).let { false }
                }
            }
        }
    }

    fun getSavedQuestions(): Set<Int> {
        return database.savedQuestionsQueries.selectAll()
            .executeAsList()
            .mapTo(HashSet(), Long::toInt)
    }

    fun getQuestionsAnswers(): Map<Int, Pair<String, Boolean>> {
        val currentTimestamp = TimeUtils.currentTimestamp()

        return database.userAnswersQueries.selectAggregatedAnswers()
            .executeAsList()
            .associate { row ->
                val questionId = row.question_id.toInt()
                val lastTreeAnswers = row.concat_is_correct.take(3)
                val isUpToDate = TimeUtils.isUpToDate(currentTimestamp, row.max_end_time)

                questionId to (lastTreeAnswers to isUpToDate)
            }
    }

    override suspend fun startNewTest(modeName: String, languageCode: String, totalQuestions: Int, maxErrors: Int): Int {
        return database.transactionWithResult {
            database.testResultsQueries.insertNewTest(
                modeName = modeName,
                languageCode = languageCode,
                totalQuestions = totalQuestions.toLong(),
                maxErrors = maxErrors.toLong(),
                startTime = TimeUtils.currentTimestamp()
            )
            database.commonQueries.getLastInsertedRowId().executeAsOne()
        }.toInt()
    }

    override suspend fun completeTest(
        id: Int,
        correctAnswers: Int,
        incorrectAnswers: Int,
        isPassed: Boolean
    ) {
        database.testResultsQueries.updateTestById(
            id = id.toLong(),
            correct_answers = correctAnswers.toLong(),
            incorrect_answers = incorrectAnswers.toLong(),
            is_passed = if (isPassed) 1 else 0,
            endTime = TimeUtils.currentTimestamp()
        )
    }

    override suspend fun saveUserAnswer(userAnswer: UserAnswer) {
        database.userAnswersQueries.insert(
            testId = userAnswer.testId.toLong(),
            questionId = userAnswer.questionId.toLong(),
            startTime = userAnswer.startTime,
            endTime = userAnswer.endTime,
            totalTime = userAnswer.totalTime,
            correctAnswer = userAnswer.correctAnswer.toString(),
            selectedAnswer = userAnswer.selectedAnswer.toString(),
            isCorrect = if (userAnswer.isCorrect) 1 else 0
        )
    }

    override suspend fun getTestResult(id: Int): TestResult? {
        return database.testResultsQueries.selectById(id = id.toLong())
            .executeAsOneOrNull()?.let { row ->
                TestResult(
                    id = row.id.toInt(),
                    modeName = row.mode_name,
                    languageCode = row.language_code,
                    totalQuestions = row.total_questions.toInt(),
                    maxErrors = row.max_errors.toInt(),
                    startTime = row.start_time,
                    endTime = row.end_time ?: 0,
                    totalTime = row.total_time ?: 0,
                    correctAnswers = row.correct_answers?.toInt() ?: 0,
                    incorrectAnswers = row.incorrect_answers?.toInt() ?: 0,
                    isPassed = row.is_passed == 1L
                )
            }
    }

    override suspend fun getUserAnswersForTest(testId: Int): List<UserAnswer> {
        return database.userAnswersQueries.selectByTestId(testId = testId.toLong())
            .executeAsList()
            .map(::mapRowToUserAnswer)
    }

    override suspend fun getUserAnswersForQuestion(questionId: Int): List<UserAnswer> {
        return database.userAnswersQueries.selectByQuestionId(questionId = questionId.toLong())
            .executeAsList()
            .map(::mapRowToUserAnswer)
    }

    private fun mapRowToUserAnswer(row: User_answers): UserAnswer {
        return UserAnswer(
            testId = row.test_id.toInt(),
            questionId = row.question_id.toInt(),
            startTime = row.start_time,
            endTime = row.end_time,
            totalTime = row.total_time,
            correctAnswer = row.correct_answer.first(),
            selectedAnswer = row.selected_answer.first(),
            isCorrect = row.is_correct == 1L
        )
    }

}