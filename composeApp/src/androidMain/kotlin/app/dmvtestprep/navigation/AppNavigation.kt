package app.dmvtestprep.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import app.dmvtestprep.domain.model.NavTab
import app.dmvtestprep.domain.usecase.UseCaseProvider
import app.dmvtestprep.ui.icons.Icons
import app.dmvtestprep.ui.screen.LearnScreen
import app.dmvtestprep.ui.screen.StatisticsScreen
import app.dmvtestprep.ui.screen.SettingsScreen
import app.dmvtestprep.ui.screen.TestConfigScreen

@Composable
fun AppNavigation() {
    val getSelectedNavTabUseCase = UseCaseProvider.getSelectedNavTabUseCase
    val saveSelectedNavTabUseCase = UseCaseProvider.saveSelectedNavTabUseCase
    var selectedNavTab by remember { mutableStateOf(getSelectedNavTabUseCase()) }

    val bottomNavVisibilityState = remember { mutableStateOf(true) }

    BackHandler(enabled = bottomNavVisibilityState.value) {
        selectedNavTab = when (selectedNavTab) {
            NavTab.LEARN -> NavTab.STATS
            NavTab.TEST -> NavTab.LEARN
            NavTab.STATS -> NavTab.TEST
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            if (bottomNavVisibilityState.value) {
                BottomNavigation {
                    NavTab.entries.forEach { navTab ->
                        BottomNavigationItem(
                            icon = { GetNavTabIcon(navTab) },
                            label = { Text(text = navTab.title, fontSize = 10.sp) },
                            selected = selectedNavTab == navTab,
                            selectedContentColor = MaterialTheme.colors.onPrimary,
                            unselectedContentColor = MaterialTheme.colors.onPrimary.copy(alpha = ContentAlpha.medium),
                            onClick = {
                                selectedNavTab = navTab
                                saveSelectedNavTabUseCase(selectedNavTab)
                            }
                        )
                    }
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                ScreenContent(selectedNavTab) { isVisible ->
                    bottomNavVisibilityState.value = isVisible
                }
            }
        }
    )
}

@Composable
fun GetNavTabIcon(navTab: NavTab) {
    when (navTab) {
        NavTab.LEARN -> Icon(Icons.Learn, contentDescription = navTab.title)
        NavTab.TEST -> Icon(Icons.Test, contentDescription = navTab.title)
        NavTab.STATS -> Icon(Icons.Stats, contentDescription = navTab.title)
    }
}

@Composable
fun ScreenContent(navTab: NavTab, onBottomNavVisibilityChanged: (Boolean) -> Unit) {
    var testTabScreen by remember { mutableStateOf(TestTabScreen.TEST_CONFIG) }

    if (navTab != NavTab.TEST) {
        testTabScreen = TestTabScreen.TEST_CONFIG
    }

    when (navTab) {
        NavTab.LEARN -> LearnScreen(onBottomNavVisibilityChanged)
        NavTab.TEST -> when (testTabScreen) {
            TestTabScreen.TEST_CONFIG -> TestConfigScreen(onBottomNavVisibilityChanged) {
                testTabScreen = TestTabScreen.SETTINGS
            }
            TestTabScreen.SETTINGS -> SettingsScreen(onBottomNavVisibilityChanged) {
                testTabScreen = TestTabScreen.TEST_CONFIG
            }
        }
        NavTab.STATS -> StatisticsScreen()
    }
}

enum class TestTabScreen {
    TEST_CONFIG, SETTINGS
}