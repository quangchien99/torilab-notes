package torilab.assessment.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import torilab.assessment.notes.data.local.preferences.AppPreferences
import torilab.assessment.notes.ui.navigation.NavGraph
import torilab.assessment.notes.ui.theme.LocalThemeState
import torilab.assessment.notes.ui.theme.ThemeState
import torilab.assessment.notes.ui.theme.ToriNotesTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkMode by appPreferences.isDarkModeFlow.collectAsState(
                initial = appPreferences.isDarkMode
            )

            val themeState = remember(isDarkMode) {
                ThemeState(
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = {
                        appPreferences.isDarkMode = !isDarkMode
                    }
                )
            }

            CompositionLocalProvider(LocalThemeState provides themeState) {
                ToriNotesTheme(darkTheme = isDarkMode) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        NavGraph()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ToriNotesTheme {
        NavGraph()
    }
}