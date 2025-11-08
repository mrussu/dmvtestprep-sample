package app.dmvtestprep.domain.usecase

import app.dmvtestprep.domain.model.StatisticsFactory
import app.dmvtestprep.domain.settings.SettingFactory
import app.dmvtestprep.repository.RepositoryProvider

object UseCaseProvider {

    private val remoteRepository get() = RepositoryProvider.remoteRepository
    private val databaseRepository get() = RepositoryProvider.databaseRepository
    private val settingsRepository get() = RepositoryProvider.settingsRepository

    val getConfigUseCase by lazy { GetConfigUseCase(remoteRepository) }
    val getLanguagesUseCase by lazy { GetLanguagesUseCase(remoteRepository) }
    val getDocumentUseCase by lazy { GetDocumentUseCase(remoteRepository) }
    val getQuestionsUseCase by lazy { GetQuestionsUseCase(remoteRepository, settingsRepository, prepareQuestionsUseCase) }
    val getTestUseCase by lazy { GetTestUseCase(remoteRepository, settingsRepository, prepareQuestionsUseCase) }
    val sendFeedbackUseCase by lazy { SendFeedbackUseCase(remoteRepository) }

    val clearStatisticsUseCase by lazy { ClearStatisticsUseCase(databaseRepository) }
    val learnedQuestionsUseCase by lazy { LearnedQuestionsUseCase(databaseRepository) }
    val prepareQuestionsUseCase by lazy { PrepareQuestionsUseCase(databaseRepository) }
    val statisticsFactory by lazy { StatisticsFactory(databaseRepository) }
    val toggleSavedQuestionUseCase by lazy { ToggleSavedQuestionUseCase(databaseRepository) }

    val getSelectedNavTabUseCase by lazy { GetSelectedNavTabUseCase(settingsRepository) }
    val settingsUseCase by lazy { SettingsUseCase(settingsRepository) }
    val saveSelectedNavTabUseCase by lazy { SaveSelectedNavTabUseCase(settingsRepository) }
    val settingFactory by lazy { SettingFactory(settingsRepository) }
    val testSettingsUseCase by lazy { TestSettingsUseCase(settingsRepository) }

}