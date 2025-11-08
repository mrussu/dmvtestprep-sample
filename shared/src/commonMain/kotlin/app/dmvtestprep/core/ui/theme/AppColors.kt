package app.dmvtestprep.core.ui.theme

import app.dmvtestprep.core.ui.model.ColorModel
import app.dmvtestprep.core.ui.model.ColorModel.Companion.rgba
import app.dmvtestprep.core.ui.model.ThemeColor

/**
 * Color definitions inspired by platform guidelines.
 *
 * References:
 * - Apple Human Interface Guidelines (HIG):
 *   https://developer.apple.com/design/human-interface-guidelines/color
 *
 * - Material Design 2:
 *   https://m2.material.io/design/color/the-color-system.html
 *
 * - Material Design 3:
 *   https://m3.material.io/styles/color/system/overview
 */

object AppColors {

    val black = ColorModel(0xFF000000, 0f, 0f, 0f, 1f)
    val white = ColorModel(0xFFFFFFFF, 1f, 1f, 1f, 1f)
    val transparent = ColorModel(0x00FFFFFF, 1f, 1f, 1f, 0f)

    val none = ThemeColor(
        light = transparent,
        dark = transparent,
        alpha = 0f
    )
    val red = ThemeColor(
        light = rgba(255, 56, 60),
        dark = rgba(255, 66, 69)
    )
    val orange = ThemeColor(
        light = rgba(255, 141, 40),
        dark = rgba(255, 146, 48)
    )
    val yellow = ThemeColor(
        light = rgba(255, 204, 0),
        dark = rgba(255, 214, 0)
    )
    val green = ThemeColor(
        light = rgba(52, 199, 89),
        dark = rgba(48, 209, 88)
    )
    val blue = ThemeColor(
        light = rgba(0, 136, 255),
        dark = rgba(0, 145, 255)
    )
    val gray = ThemeColor(
        light = rgba(142, 142, 147),
        dark = rgba(142, 142, 147)
    )
    val gray2 = ThemeColor(
        light = rgba(174, 174, 178),
        dark = rgba(99, 99, 102)
    )
    val gray3 = ThemeColor(
        light = rgba(199, 199, 204),
        dark = rgba(72, 72, 74)
    )
    val gray4 = ThemeColor(
        light = rgba(209, 209, 214),
        dark = rgba(58, 58, 60)
    )
    val gray5 = ThemeColor(
        light = rgba(229, 229, 234),
        dark = rgba(44, 44, 46)
    )
    val gray6 = ThemeColor(
        light = rgba(242, 242, 247),
        dark = rgba(28, 28, 30)
    )
    val link = ThemeColor(
        light = rgba(88, 86, 214),
        dark = rgba(94, 92, 230)
    )

    val primary = blue
    val secondary = green
    val background = ThemeColor(
        light = rgba(243, 242, 248),
        dark = gray6.dark
    )
    val surface = ThemeColor(
        light = white,
        dark = gray5.dark
    )
    val error = red
    val onPrimary = ThemeColor(
        light = white,
        dark = white
    )
    val onSecondary = ThemeColor(
        light = white,
        dark = white
    )
    val onBackground = ThemeColor(
        light = gray5.dark,
        dark = gray5.light
    )
    val onSurface = ThemeColor(
        light = gray4.dark,
        dark = gray4.light
    )
    val onError = ThemeColor(
        light = white,
        dark = white
    )

    val success = green
    val warning = orange
    val failure = red

}