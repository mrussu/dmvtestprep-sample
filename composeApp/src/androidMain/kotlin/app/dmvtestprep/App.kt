package app.dmvtestprep

import androidx.compose.runtime.Composable
import app.dmvtestprep.navigation.AppNavigation
import app.dmvtestprep.ui.theme.AppTheme

@Composable
fun App() {
    AppTheme {
        AppNavigation()
    }
}