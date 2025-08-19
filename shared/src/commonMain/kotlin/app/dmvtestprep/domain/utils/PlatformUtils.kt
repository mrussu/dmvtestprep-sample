package app.dmvtestprep.domain.utils

interface Platform {
    val name: String
    val version: String
    val manufacturer: String
    val model: String

    fun systemInfo(): String = "$name $version"
    fun deviceInfo(): String = "$manufacturer $model"
}

expect val platform: Platform