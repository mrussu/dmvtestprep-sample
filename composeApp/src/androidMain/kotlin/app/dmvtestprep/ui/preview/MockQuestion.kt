package app.dmvtestprep.ui.preview

import app.dmvtestprep.domain.model.BilingualText
import app.dmvtestprep.domain.model.LearningStage
import app.dmvtestprep.domain.model.Question
import app.dmvtestprep.domain.model.QuestionNumber
import app.dmvtestprep.domain.model.toAnswerChar

object MockQuestion {
    val question25 = Question(
        id = 25,
        num = QuestionNumber(
            chapter = 2,
            question = 25
        ),
        text = BilingualText(
            english = "This sign means:",
            native = "Этот знак означает:"
        ),
        answers = listOf(
            Question.Answer(
                prefix = "",
                char = 'A',
                text = BilingualText(
                    english = "Divided highway ends",
                    native = "Конец разделенной автомагистрали"
                )
            ),
            Question.Answer(
                prefix = "",
                char = 'B',
                text = BilingualText(
                    english = "One-way street begins",
                    native = "Начало дороги с односторонним движением"
                )
            ),
            Question.Answer(
                prefix = "",
                char = 'C',
                text = BilingualText(
                    english = "One-way street ends",
                    native = "Конец дороги с односторонним движением"
                )
            ),
            Question.Answer(
                prefix = "",
                char = 'D',
                text = BilingualText(
                    english = "Divided highway begins",
                    native = "Начало разделенной автомагистрали"
                )
            )
        ),
        correctAnswerChar = 'D'.toAnswerChar(),
        image = "/images/questions/warning_sign_13.png",
        isSaved = false,
        learningStage = LearningStage(
            type = LearningStage.Type.LEARNED,
            isUpToDate = true
        )
    )
    val question167 = Question(
        id = 167,
        num = QuestionNumber(
            chapter = 3,
            question = 109
        ),
        text = BilingualText(
            english = "When you are in a line of traffic that is crossing a railroad track that has no signals or gates:",
            native = "Если вы едете в потоке автомобилей, проезжающих железнодорожный переезд без светофора и шлагбаума:"
        ),
        answers = listOf(
            Question.Answer(
                prefix = "",
                char = 'A',
                text = BilingualText(
                    english = "You have the right of way and do not need to check for trains",
                    native = "Вы имеете право преимущественного проезда и не должны следить за приближением поезд"
                )
            ),
            Question.Answer(
                prefix = "",
                char = 'B',
                text = BilingualText(
                    english = "You may pass slower drivers crossing the track",
                    native = "Вы можете обгонять автомобили, переезжающие железнодорожные пути с меньшей скоростью"
                )
            ),
            Question.Answer(
                prefix = "",
                char = 'C',
                text = BilingualText(
                    english = "You need to make sure there is space to get all the way across the tracks without stopping, before you start to cross",
                    native = "Перед тем как начать переезжать железнодорожные пути, вы должны убедиться, что перед вами имеется достаточное свободное пространство для того, чтобы переехать железнодорожные пути без остановки"
                )
            ),
            Question.Answer(
                prefix = "",
                char = 'D',
                text = BilingualText(
                    english = "All of the above",
                    native = "Все перечисленное выше"
                )
            ),
        ),
        correctAnswerChar = 'C'.toAnswerChar(),
        image = null,
        isSaved = true,
        learningStage = LearningStage(
            type = LearningStage.Type.EMPTY,
            isUpToDate = false
        )
    )
}