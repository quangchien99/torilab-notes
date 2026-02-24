package torilab.assessment.notes.ui.screen.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import torilab.assessment.notes.R
import torilab.assessment.notes.ui.theme.ToriNotesTheme

@Composable
fun DeleteConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    noteCount: Int = 1
) {
    val title = if (noteCount <= 1)
        stringResource(R.string.dialog_title_delete_note)
    else
        stringResource(R.string.dialog_title_delete_notes, noteCount)

    val message = if (noteCount <= 1)
        stringResource(R.string.dialog_message_delete_note)
    else
        stringResource(R.string.dialog_message_delete_notes, noteCount)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.action_delete),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.action_cancel))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun DeleteConfirmDialogSinglePreview() {
    ToriNotesTheme(dynamicColor = false) {
        DeleteConfirmDialog(
            onConfirm = {},
            onDismiss = {},
            noteCount = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteConfirmDialogMultiplePreview() {
    ToriNotesTheme(dynamicColor = false) {
        DeleteConfirmDialog(
            onConfirm = {},
            onDismiss = {},
            noteCount = 5
        )
    }
}
