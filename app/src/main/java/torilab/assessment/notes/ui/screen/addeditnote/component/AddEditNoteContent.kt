package torilab.assessment.notes.ui.screen.addeditnote.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import torilab.assessment.notes.R
import torilab.assessment.notes.ui.screen.addeditnote.AddEditNoteState
import torilab.assessment.notes.ui.screen.component.DeleteConfirmDialog
import torilab.assessment.notes.ui.theme.ToriNotesTheme

@Composable
internal fun AddEditNoteContent(
    state: AddEditNoteState,
    snackbarHostState: SnackbarHostState,
    showDeleteDialog: Boolean,
    onNavigateBack: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onContentChanged: (String) -> Unit,
    onSave: () -> Unit,
    onDeleteRequest: () -> Unit,
    onDeleteConfirm: () -> Unit,
    onDeleteDismiss: () -> Unit
) {
    val isSaveEnabled = state.title.isNotBlank()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .imePadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.content_description_back),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Text(
                    text = stringResource(
                        if (state.isEditMode) R.string.screen_title_edit_note
                        else R.string.screen_title_add_edit_note
                    ),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )

                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(end = 12.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    if (state.isEditMode) {
                        IconButton(onClick = onDeleteRequest) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = stringResource(R.string.content_description_delete),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    IconButton(
                        onClick = onSave,
                        enabled = isSaveEnabled
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.content_description_save),
                            tint = if (isSaveEnabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }

            NoteTextField(
                value = state.title,
                onValueChange = onTitleChanged,
                placeholder = stringResource(R.string.hint_title),
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            NoteTextField(
                value = state.content,
                onValueChange = onContentChanged,
                placeholder = stringResource(R.string.hint_content),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .imePadding()
        )
    }

    if (showDeleteDialog) {
        DeleteConfirmDialog(
            onConfirm = onDeleteConfirm,
            onDismiss = onDeleteDismiss
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddEditNoteNewPreview() {
    ToriNotesTheme(dynamicColor = false) {
        AddEditNoteContent(
            state = AddEditNoteState(),
            snackbarHostState = SnackbarHostState(),
            showDeleteDialog = false,
            onNavigateBack = {},
            onTitleChanged = {},
            onContentChanged = {},
            onSave = {},
            onDeleteRequest = {},
            onDeleteConfirm = {},
            onDeleteDismiss = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddEditNoteEditPreview() {
    ToriNotesTheme(dynamicColor = false) {
        AddEditNoteContent(
            state = AddEditNoteState(
                title = "Meeting Notes",
                content = "Discuss project timeline, assign tasks to team members, and review the Q2 budget report.",
                isEditMode = true,
                noteId = 1L
            ),
            snackbarHostState = SnackbarHostState(),
            showDeleteDialog = false,
            onNavigateBack = {},
            onTitleChanged = {},
            onContentChanged = {},
            onSave = {},
            onDeleteRequest = {},
            onDeleteConfirm = {},
            onDeleteDismiss = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AddEditNoteEditDarkPreview() {
    ToriNotesTheme(dynamicColor = false, darkTheme = true) {
        AddEditNoteContent(
            state = AddEditNoteState(
                title = "Meeting Notes",
                content = "Discuss project timeline, assign tasks to team members, and review the Q2 budget report.",
                isEditMode = true,
                noteId = 1L
            ),
            snackbarHostState = SnackbarHostState(),
            showDeleteDialog = false,
            onNavigateBack = {},
            onTitleChanged = {},
            onContentChanged = {},
            onSave = {},
            onDeleteRequest = {},
            onDeleteConfirm = {},
            onDeleteDismiss = {}
        )
    }
}
