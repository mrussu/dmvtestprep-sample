package app.dmvtestprep.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Language Codes according to ISO 639-3
 * https://en.wikipedia.org/wiki/List_of_ISO_639_language_codes
 */

@Serializable
data class LanguageData(
    val code: String,
    val name: String,
    @SerialName("native_name")
    val nativeName: String? = null
)