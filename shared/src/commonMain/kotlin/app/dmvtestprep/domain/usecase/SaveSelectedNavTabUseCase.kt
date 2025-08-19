package app.dmvtestprep.domain.usecase

import app.dmvtestprep.domain.model.NavTab
import app.dmvtestprep.repository.SettingsRepository

class SaveSelectedNavTabUseCase(private val settingsRepository: SettingsRepository) {

    operator fun invoke(navTab: NavTab) {
        settingsRepository.savaNavTab(navTab)
    }

}