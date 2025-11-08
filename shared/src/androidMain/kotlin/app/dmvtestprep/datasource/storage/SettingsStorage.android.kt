package app.dmvtestprep.datasource.storage

import android.content.Context
import android.content.SharedPreferences

class SettingsStorageAndroid(context: Context) : SettingsStorage {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)

    override fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    override fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    override fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    override fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    override fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

}

private var settingsStorageInstance: SettingsStorage? = null

fun initializeSettingsStorage(context: Context) {
    if (settingsStorageInstance == null) {
        settingsStorageInstance = SettingsStorageAndroid(context)
    }
}

actual val settingsStorage: SettingsStorage
    get() = settingsStorageInstance ?: throw IllegalStateException("SettingsStorage must be initialized.")