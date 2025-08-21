package org.ghost.notes.ui.screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import org.ghost.notes.entity.Note
import org.ghost.notes.viewModels.NotesListViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NoteListScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesListViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNoteClick: (Int) -> Unit = {},
    onAddNewClick: () -> Unit = {}
) {
    val notes: LazyPagingItems<Note> = viewModel.notes.collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier,
        topBar = { NotesTopAppBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNewClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        LazyColumn(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Weekly notes",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            items(
                count = notes.itemCount,
                key = { index -> notes[index]?.id ?: index }
            ) { index ->
                notes[index]?.let { note ->
                    NoteItemCard(
                        modifier = Modifier.fillMaxWidth(),
                        note = note,
                        onClick = onNoteClick,
                        onDelete = viewModel::deleteNote,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }

            }
        }
    }
}