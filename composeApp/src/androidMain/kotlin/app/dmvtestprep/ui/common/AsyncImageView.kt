package app.dmvtestprep.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                    tint = Color.Gray,
                    contentDescription = "Image loading error",
                    modifier = Modifier.size(48.dp)
                )
            }
        },
        contentDescription = "Question Image",
        modifier = modifier
    )
}