package app.dmvtestprep.domain.model

data class BilingualText(
    override val english: String,
    override val native: String? = null
) : TextProvider