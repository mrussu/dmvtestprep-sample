package app.dmvtestprep.datasource.storage

import platform.Foundation.NSUserDefaults

class SettingsStorageIOS : SettingsStorage {

    private val userDefaults = NSUserDefaults.standardUserDefaults

    override fun getInt(key: String, defaultValue: Int): Int {
        return if (userDefaults.objectForKey(key) != null) {
            userDefaults.integerForKey(key).toInt()
        } else {
            defaultValue
        }
    }

    override fun putInt(key: String, value: Int) {
        userDefaults.setInteger(value.toLong(), key)
    }

    override fun getString(key: String, defaultValue: String): String {
        return userDefaults.stringForKey(key) ?: defaultValue
    }

    override fun putString(key: String, value: String) {
        userDefaults.setObject(value, key)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return if (userDefaults.objectForKey(key) != null) {
            userDefaults.boolForKey(key)
        } else {
            defaultValue
        }
    }

    override fun putBoolean(key: String, value: Boolean) {
        userDefaults.setBool(value, key)
    }

}

actual val settingsStorage: SettingsStorage = SettingsStorageIOS()