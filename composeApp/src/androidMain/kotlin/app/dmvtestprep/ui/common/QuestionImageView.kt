package app.dmvtestprep.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuestionImageView(imageUrl: String) {
    AsyncImageView(
        imageUrl = imageUrl,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(160.dp)
    )
}