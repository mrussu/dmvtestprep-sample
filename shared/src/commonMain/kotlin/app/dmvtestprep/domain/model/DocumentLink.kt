package app.dmvtestprep.domain.model

data class DocumentLink(
    override val id: Int,
    val title: String
) : Entity