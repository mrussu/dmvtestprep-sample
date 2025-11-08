package app.dmvtestprep.datasource.storage

interface SettingsStorage {

    fun getInt(key: String, defaultValue: Int): Int
    fun putInt(key: String, value: Int)

    fun getString(key: String, defaultValue: String): String
    fun putString(key: String, value: String)

    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun putBoolean(key: String, value: Boolean)

}

expect val settingsStorage: SettingsStorage