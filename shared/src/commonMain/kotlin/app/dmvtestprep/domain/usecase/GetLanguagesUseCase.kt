package app.dmvtestprep.domain.usecase

import app.dmvtestprep.domain.model.Language
import app.dmvtestprep.repository.RemoteRepository

class GetLanguagesUseCase(private val remoteRepository: RemoteRepository) {

    private var languages: List<Language>? = null

    suspend operator fun invoke(): List<Language> {
        return languages ?: fetchLanguages()
    }

    private suspend fun fetchLanguages(): List<Language> {
        return remoteRepository.getLanguages().also {
            languages = it
        }
    }

}