package app.dmvtestprep.domain.settings

import app.dmvtestprep.repository.SettingsRepository
import kotlin.jvm.JvmName

class SettingFactory(private val settingsRepository: SettingsRepository) {

    @JvmName("createIntSetting")
    fun createSetting(settingConfig: SettingConfig<Int>): Setting<Int> {
        return createSetting(
            settingConfig = settingConfig,
            storedValue = settingsRepository.getSettingValue(settingConfig.defaultSetting)
        )
    }

    @JvmName("createStringSetting")
    fun createSetting(settingConfig: SettingConfig<String>): Setting<String> {
        return createSetting(
            settingConfig = settingConfig,
            storedValue = settingsRepository.getSettingValue(settingConfig.defaultSetting)
        )
    }

    @JvmName("createBooleanSetting")
    fun createSetting(settingConfig: SettingConfig<Boolean>): Setting<Boolean> {
        return createSetting(
            settingConfig = settingConfig,
            storedValue = settingsRepository.getSettingValue(settingConfig.defaultSetting)
        )
    }

    companion object {
        fun <T : Any> defaultSetting(settingConfig: SettingConfig<T>): Setting<T> {
            return createSetting(settingConfig)
        }

        private fun <T : Any> createSetting(settingConfig: SettingConfig<T>, storedValue: T? = null): Setting<T> {
            val value = storedValue ?: settingConfig.defaultSetting.defaultValue

            return Setting(
                value = value,
                label = settingConfig.label,
                details = settingConfig.details,
                isEnabled = settingConfig.isEnabled
            )
        }
    }
}