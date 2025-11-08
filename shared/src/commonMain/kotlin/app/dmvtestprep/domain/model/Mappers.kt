package app.dmvtestprep.domain.model

import app.dmvtestprep.core.utils.toMarkdown
import app.dmvtestprep.model.*

fun LanguageData.toLanguage(): Language {
    return Language(
        code = this.code,
        name = BilingualText(
            english = this.name,
            native = this.nativeName
        )
    )
}

fun List<LanguageData>.toLanguages(): List<Language> {
    return this.map { it.toLanguage() }
}

fun TestData.toTest(): Test {
    return Test(
        modeName = this.modeName,
        languageCode = this.languageCode,
        questions = this.questions.map { it.toQuestion() },
        settings = TestSettings.default(this.modeName)
    )
}

fun QuestionData.toQuestion(): Question {
    return Question(
        id = this.id,
        num = QuestionNumber(
            chapter = this.chapter,
            question = this.question
        ),
        text = BilingualText(
            english = this.english,
            native = this.native
        ),
        image = this.image,
        answers = this.answers.map { it.toAnswer() },
        correctAnswerChar = this.answer.toAnswerChar()
    )
}

fun QuestionData.AnswerData.toAnswer(): Question.Answer {
    return Question.Answer(
        prefix = "",
        char = this.char,
        text = BilingualText(
            english = this.english,
            native = this.native
        )
    )
}

fun Char.toAnswerChar(): AnswerChar.Value {
    return AnswerChar.Value(this)
}

fun DocumentData.toDocument(): DocumentState.Document {
    return DocumentState.Document(
        header = this.title,
        content = this.text.toMarkdown()
    )
}

fun Feedback.toBodyData(usageContext: UsageContext): BodyData<FeedbackData> {
    return FeedbackData(
        message = message
    ).toBodyData(usageContext)
}

fun TestResult.toBodyData(usageContext: UsageContext): BodyData<TestResultData> {
    return TestResultData(
        modeName = modeName,
        languageCode = languageCode,
        totalQuestions = totalQuestions,
        maxErrors = maxErrors,
        startTime = startTime,
        endTime = endTime,
        totalTime = totalTime,
        correctAnswers = correctAnswers,
        incorrectAnswers = incorrectAnswers,
        isPassed = isPassed,
        accuracy = accuracy
    ).toBodyData(usageContext)
}

fun TestResultResponseData.toTestResultResponse(): TestResultResponse {
    return when (status.lowercase()) {
        "success" -> TestResultResponse.Success(headerText, inspireText)
        else -> TestResultResponse.Failure
    }
}

fun FeedbackResponseData.toFeedbackResponse(): FeedbackResponse {
    return when (status.lowercase()) {
        "success" -> FeedbackResponse.Success(message)
        else -> FeedbackResponse.Failure(message)
    }
}

fun <T> T.toBodyData(usageContext: UsageContext): BodyData<T> {
    return BodyData(
        data = this,
        usageContext = UsageContextData(
            appVersion = usageContext.appVersion,
            systemInfo = usageContext.systemInfo,
            deviceInfo = usageContext.deviceInfo
        )
    )
}

fun <T : Entity> List<T>.toListNavigator(initialIndex: Int = 0): ListNavigator<T> {
    return ListNavigator(this, initialIndex.coerceIn(0, lastIndex.takeIf { isNotEmpty() } ?: 0))
}