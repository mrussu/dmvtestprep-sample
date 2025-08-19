package app.dmvtestprep.domain.usecase

import app.dmvtestprep.domain.model.NavTab
import app.dmvtestprep.repository.SettingsRepository

class GetSelectedNavTabUseCase(private val settingsRepository: SettingsRepository) {

    operator fun invoke(): NavTab {
        return settingsRepository.getNavTab()
    }

}