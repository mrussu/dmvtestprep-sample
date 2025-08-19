package app.dmvtestprep.ui.preview

import app.dmvtestprep.domain.model.Test
import app.dmvtestprep.domain.model.TestSettings

object MockTest {
    val test1 = Test(
        modeName = "exam",
        languageCode = "eng",
        questions = listOf(
            MockQuestion.question25,
            MockQuestion.question167
        ),
        settings = TestSettings.default("exam")
    )
}