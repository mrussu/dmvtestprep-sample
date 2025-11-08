package app.dmvtestprep.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.dmvtestprep.ui.icons.Icons
import coil.compose.SubcomposeAsyncImage

@Composable
fun AsyncImageView(
    imageUrl: String,
    modifier: Modifier
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        error = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = Icons.ImageError,
                    contentDescription = "Image loading error",
                    tint = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
                    modifier = Modifier.size(48.dp)
                )
            }
        },
        contentDescription = "Question Image",
        modifier = modifier
    )
}