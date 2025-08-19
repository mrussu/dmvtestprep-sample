package app.dmvtestprep.domain.settings

data class Setting<T : Any>(
    val value: T,
    val label: String,
    val details: String = "",
    val isEnabled: Boolean = true
) {
    fun updateEnabled(isEnabled: Boolean): Setting<T> {
        return this.copy(isEnabled = isEnabled)
    }
}