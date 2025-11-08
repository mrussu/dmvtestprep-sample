package app.dmvtestprep.ui.common

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.dmvtestprep.ui.icons.Icons

@Composable
fun ToggleSavedButton(
    questionId: Int,
    isSaved: Boolean,
    modifier: Modifier,
    onToggleSave: (Int) -> Unit
) {
    IconToggleButton(
        checked = isSaved,
        onCheckedChange = {
            onToggleSave(questionId)
        },
        modifier = modifier
    ) {
        Icon(
            painter = if (isSaved) Icons.BookmarkFilled else Icons.BookmarkOutlined,
            contentDescription = if (isSaved) "Remove from saved" else "Save",
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.size(24.dp)
        )
    }
}