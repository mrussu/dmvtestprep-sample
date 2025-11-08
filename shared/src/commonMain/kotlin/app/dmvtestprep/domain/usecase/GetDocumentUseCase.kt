package app.dmvtestprep.domain.usecase

import app.dmvtestprep.domain.model.DocumentState.Document
import app.dmvtestprep.repository.RemoteRepository

class GetDocumentUseCase(private val remoteRepository: RemoteRepository) {

    private var cachedDocuments: MutableMap<Int, Document> = mutableMapOf()

    suspend operator fun invoke(id: Int): Document {
        return cachedDocuments[id] ?: fetchDocument(id)
    }

    private suspend fun fetchDocument(id: Int): Document {
        return remoteRepository.getDocument(id).also {
            cachedDocuments[id] = it
        }
    }

}