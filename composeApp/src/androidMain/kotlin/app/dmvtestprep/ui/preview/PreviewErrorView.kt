package app.dmvtestprep.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.dmvtestprep.domain.model.ErrorTypes
import app.dmvtestprep.ui.common.ErrorView

@Preview
@Composable
fun PreviewErrorView() {
    ErrorView(
        errorSummary = ErrorTypes.Network.NoInternetConnection.toErrorSummary(),
        onRetry = {}
    )
}