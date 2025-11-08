package app.dmvtestprep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.dmvtestprep.datasource.storage.initializeSettingsStorage
import app.dmvtestprep.datasource.storage.initializeSqlDriver

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeSettingsStorage(this)
        initializeSqlDriver(this)

        setContent {
            App()
        }
    }

}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}