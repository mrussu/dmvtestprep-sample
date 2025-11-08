package app.dmvtestprep.domain.usecase

import app.dmvtestprep.core.utils.reshuffle
import app.dmvtestprep.domain.model.LearningStage
import app.dmvtestprep.domain.model.Question
import app.dmvtestprep.repository.DatabaseRepository

class PrepareQuestionsUseCase(private val databaseRepository: DatabaseRepository) {

    fun prepare(
        questions: List<Question>,
        setLearningStage: Boolean,
        skipLearnedQuestions: Boolean,
        shuffleQuestions: Boolean,
        shuffleAnswers: Boolean,
        answerPrefix: Boolean
    ): List<Question> {
        val savedQuestions = databaseRepository.getSavedQuestions()
        val questionsAnswers = if (setLearningStage) databaseRepository.getQuestionsAnswers() else emptyMap()
        val idsToSkip = if (skipLearnedQuestions) databaseRepository.getLearnedQuestionsIds() else emptySet()

        return questions
            .filter { question -> question.id !in idsToSkip }
            .let { if (shuffleQuestions) it.reshuffle() else it }
            .map { question ->
                question.copy(
                    answers = prepareAnswers(question.answers, shuffleAnswers, answerPrefix),
                    isSaved = question.id in savedQuestions,
                    learningStage = getLearningStage(questionsAnswers[question.id])
                )
            }
    }

    private fun prepareAnswers(
        answers: List<Question.Answer>,
        shuffleAnswers: Boolean,
        answerPrefix: Boolean
    ): List<Question.Answer> {
       return answers
           .let { if (shuffleAnswers) it.reshuffle() else it }
           .run {
               when {
                   !answerPrefix -> this
                   !shuffleAnswers -> map { answer -> answer.copy(prefix = "${answer.char}. ") }
                   else -> mapIndexed { index, answer -> answer.copy(prefix = "${index + 1}. ") }
               }
           }
    }

    private fun getLearningStage(questionAnswers: Pair<String, Boolean>?): LearningStage {
        return questionAnswers?.let { (sequenceOfAnswers, isUpToDate) ->
            LearningStage.determineStage(sequenceOfAnswers, isUpToDate)
        } ?: LearningStage()
    }

}