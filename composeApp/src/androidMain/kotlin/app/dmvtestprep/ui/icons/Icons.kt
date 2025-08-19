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
    val Profile: Painter
        @Composable
        get() = painterResource(id = R.drawable.ic_profile)
    val EnglishMode: Painter
        @Composable
        get() = painterResource(id = R.drawable.ic_english_mode)
    val Settings: Painter
        @Composable
        get() = painterResource(id = R.drawable.ic_settings)
    val ArrowBack: Painter
        @Composable
        get() = painterResource(id = R.drawable.ic_arrow_back)
    val Save: Painter
        @Composable
        get() = painterResource(id = R.drawable.is_save)
    val Saved: Painter
        @Composable
        get() = painterResource(id = R.drawable.ic_saved)
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