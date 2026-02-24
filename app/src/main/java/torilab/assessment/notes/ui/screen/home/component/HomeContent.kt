package torilab.assessment.notes.ui.screen.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import torilab.assessment.notes.R
import torilab.assessment.notes.domain.model.Note
import torilab.assessment.notes.ui.screen.component.NoteCard
import torilab.assessment.notes.ui.theme.ToriNotesTheme

@Composable
internal fun HomeContent(
    notes: LazyPagingItems<Note>,
    isSearching: Boolean,
    isSelectionMode: Boolean,
    selectedNoteIds: Set<Long>,
    onNoteClick: (Long) -> Unit,
    onNoteLongClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    onShareClick: (Note) -> Unit,
    onDeleteClick: (Note) -> Unit
) {
    when (notes.loadState.refresh) {
        is LoadState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is LoadState.NotLoading if notes.itemCount == 0 -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(
                        if (isSearching) R.string.empty_search_results
                        else R.string.empty_notes
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        else -> {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = notes.itemCount,
                    key = { index -> notes[index]?.id ?: index }
                ) { index ->
                    notes[index]?.let { note ->
                        NoteCard(
                            note = note,
                            onClick = { onNoteClick(note.id) },
                            onEditClick = { onEditClick(note.id) },
                            onShareClick = { onShareClick(note) },
                            onDeleteClick = { onDeleteClick(note) },
                            isSelectionMode = isSelectionMode,
                            isSelected = selectedNoteIds.contains(note.id),
                            onLongClick = { onNoteLongClick(note.id) }
                        )
                    }
                }

                if (notes.loadState.append is LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeScreenPreviewContent(
    notes: List<Note>,
    isSelectionMode: Boolean = false,
    selectedNoteIds: Set<Long> = emptySet(),
    searchQuery: String = ""
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedVisibility(
            visible = isSelectionMode,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            SelectionTopBar(
                selectedCount = selectedNoteIds.size,
                onClose = {},
                onSelectAll = {},
                onDelete = {}
            )
        }

        AnimatedVisibility(visible = !isSelectionMode) {
            Column {
                Text(
                    text = stringResource(R.string.screen_title_home),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(
                        start = 16.dp, top = 16.dp, end = 16.dp,
                        bottom = if (notes.isNotEmpty()) 4.dp else 8.dp
                    )
                )

                if (notes.isNotEmpty() || searchQuery.isNotBlank()) {
                    NoteSearchBar(
                        query = searchQuery,
                        onQueryChange = {},
                        onClear = {}
                    )
                }
            }
        }

        if (notes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.empty_notes),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = notes.size,
                    key = { index -> notes[index].id }
                ) { index ->
                    NoteCard(
                        note = notes[index],
                        onClick = {},
                        onEditClick = {},
                        onShareClick = {},
                        onDeleteClick = {},
                        isSelectionMode = isSelectionMode,
                        isSelected = selectedNoteIds.contains(notes[index].id)
                    )
                }
            }
        }
    }
}

private val previewNotes = listOf(
    Note(id = 1, title = "Meeting Notes", content = "Discuss project timeline and assign tasks.", createdAt = 1708761600000, updatedAt = 1708761600000),
    Note(id = 2, title = "Shopping List", content = "Milk, eggs, bread, butter, coffee beans.", createdAt = 1708675200000, updatedAt = 1708675200000),
    Note(id = 3, title = "Ideas for App", content = "Add dark mode, offline sync, and push notifications.", createdAt = 1708588800000, updatedAt = 1708588800000),
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenWithNotesPreview() {
    ToriNotesTheme(dynamicColor = false) {
        HomeScreenPreviewContent(notes = previewNotes)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenEmptyPreview() {
    ToriNotesTheme(dynamicColor = false) {
        HomeScreenPreviewContent(notes = emptyList())
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenSelectionModePreview() {
    ToriNotesTheme(dynamicColor = false) {
        HomeScreenPreviewContent(
            notes = previewNotes,
            isSelectionMode = true,
            selectedNoteIds = setOf(1L, 3L)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenDarkPreview() {
    ToriNotesTheme(dynamicColor = false, darkTheme = true) {
        HomeScreenPreviewContent(notes = previewNotes)
    }
}
