package app.dmvtestprep.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.dmvtestprep.domain.settings.SettingConfig
import app.dmvtestprep.domain.settings.SettingFactory
import app.dmvtestprep.ui.common.SettingToggle

@Preview
@Composable
fun PreviewSettingSwitch() {
    SettingToggle(
        setting = SettingFactory.defaultSetting(SettingConfig.AnswerPrefix),
        onToggle = {},
        isLabelBold = true
    )
}