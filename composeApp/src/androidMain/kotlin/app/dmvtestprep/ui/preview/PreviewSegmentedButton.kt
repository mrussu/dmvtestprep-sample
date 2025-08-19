package app.dmvtestprep.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.dmvtestprep.domain.model.QuestionFilter
import app.dmvtestprep.ui.common.SegmentedButton

@Preview
@Composable
fun PreviewSegmentedButton() {
    val questionFilter = QuestionFilter.entries

    SegmentedButton(
        options = questionFilter,
        selectedOption = questionFilter.first(),
        onOptionSelected = {},
        getLabel = { it.filterName }
    )
}