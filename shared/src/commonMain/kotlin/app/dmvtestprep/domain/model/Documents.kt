package app.dmvtestprep.domain.model

object Documents {
    val legal by lazy {
        listOf(
            DocumentLink(1, "Terms and Conditions"),
            DocumentLink(2, "Privacy Notice"),
            DocumentLink(3, "Disclaimer")
        )
    }
}