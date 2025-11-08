package app.dmvtestprep.ui.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import app.dmvtestprep.R
import androidx.compose.ui.res.painterResource

object Icons {

    val Learn: Painter
        @Composable
        get() = painterResource(id = R.drawable.ic_learn)
    val Test: Painter
        @Composable
        get() = painterResource(id = R.drawable.ic_test)
    val Stats: Painter
        @Composable
        get() = painterResource(id = R.drawable.ic_stats)
    val EnglishModeOutlined: Painter
        @Composable
        get() = painterResource(id = R.drawable.ic_english_mode_outlined)
    val EnglishModeFilled: Painter
        @Composable
        get() = painterResource(id = R.drawable.ic_english_mode_filled)
    val Settings: Painter
        @Composable
        get() = painterResource(id = R.drawable.ic_settings)
    val ArrowBack: Painter
        @Composable
        get() = painterResource(id = R.drawable.ic_arrow_back)
    val BookmarkOutlined: Painter
        @Composable
        get() = painterResource(id = R.drawable.ic_bookmark_outlined)
    val BookmarkFilled: Painter
        @Composable
        get() = painterResource(id = R.drawable.ic_bookmark_filled)
    val ImageError: Painter
        @Composable
        get() = painterResource(id = R.drawable.ic_image_error)

}