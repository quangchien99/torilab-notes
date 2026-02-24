package torilab.assessment.notes.ui.screen.settings

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import torilab.assessment.notes.R
import torilab.assessment.notes.ui.screen.settings.component.SettingsContent

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val doneMessage = stringResource(R.string.settings_generate_done)

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SettingsEvent.BulkNotesGenerated -> snackbarHostState.showSnackbar(doneMessage)
                else -> Unit
            }
        }
    }

    SettingsContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onGenerateBulkNotes = { viewModel.onTriggerEvent(SettingsEvent.GenerateBulkNotes) }
    )
}
