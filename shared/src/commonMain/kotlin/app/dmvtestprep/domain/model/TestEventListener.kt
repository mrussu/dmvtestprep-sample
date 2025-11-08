package app.dmvtestprep.domain.model

interface TestEventListener {
    suspend fun handle(event: TestEvent)
}