package app.dmvtestprep.viewmodel

import app.dmvtestprep.domain.model.ErrorSummary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

abstract class BaseViewModel {
    private val job = SupervisorJob()
    private val baseScope = CoroutineScope(job + Dispatchers.Main.immediate)

    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        baseScope.launch(block = block)
    }

    protected fun toErrorSummary(message: String?): ErrorSummary {
        return ErrorSummary(
            type = "Oops!",
            error = "Something went wrong",
            details = message ?: "Please try again later."
        )
    }

    open fun clear() {
        job.cancel()
    }
}