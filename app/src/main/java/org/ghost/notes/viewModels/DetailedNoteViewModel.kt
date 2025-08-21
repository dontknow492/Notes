package org.ghost.notes.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ghost.notes.entity.Note
import org.ghost.notes.entity.Tag
import org.ghost.notes.repository.NotesRepository
import org.ghost.notes.state.DetailedNoteUiState
import javax.inject.Inject


@HiltViewModel
class DetailedNoteViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val notesRepository: NotesRepository
) : ViewModel() {

    // Get the noteId, defaulting to -1 if it's not passed (our "create mode" signal)
    private val noteId: Int = savedStateHandle.get<Int>("noteId") ?: -1

    private val _uiState = MutableStateFlow(DetailedNoteUiState())
    val uiState: StateFlow<DetailedNoteUiState> = _uiState.asStateFlow()

    init {
        // Check if we are in "edit mode" or "create mode"
        if (noteId != -1) {
            // --- EDIT MODE ---
            // Same logic as before: load the existing note
            viewModelScope.launch {
                notesRepository.getDetailedNote(noteId).collectLatest { noteWithTags ->
                    if (noteWithTags != null) {
                        _uiState.update { currentState ->
                            currentState.copy(
                                originalNote = noteWithTags.note, // Store original
                                heading = noteWithTags.note.heading,
                                title = noteWithTags.note.title ?: "",
                                body = noteWithTags.note.body ?: "",
                                image = noteWithTags.note.image,
                                color = noteWithTags.note.color,
                                isPinned = noteWithTags.note.isPinned,
                                themeId = noteWithTags.note.themeId,
                                tags = noteWithTags.tags,
                                date = noteWithTags.note.createdAt,
                                // ...copy other fields...
                                isLoading = false,
                                isEditing = false // Start in view mode
                            )
                        }
                    }
                }
            }
        } else {
            // --- CREATE MODE ---
            // Start with a blank slate, ready for editing
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isEditing = true // Start immediately in edit mode
                )
            }
        }
    }

    // ... onTitleChange, onContentChange, etc. are the same ...

    fun saveNote() {
        onEditModeChanged(false)
        viewModelScope.launch {

            val currentState = _uiState.value

            if (noteId != -1) {
                // --- UPDATE EXISTING NOTE ---
                val originalNote = currentState.originalNote ?: return@launch
                val updatedNote = originalNote.copy(
                    heading = currentState.heading,
                    title = currentState.title,
                    body = currentState.body,
                    updatedAt = System.currentTimeMillis(),
                    image = currentState.image,
                    color = currentState.color,
                    isPinned = currentState.isPinned,
                    themeId = currentState.themeId
                    // ...copy other updated fields...
                )
                notesRepository.updateNoteWithTags(updatedNote, currentState.tags)

            } else {
                // --- INSERT NEW NOTE With TAGS ---
                val newNote = Note(
                    heading = currentState.heading,
                    title = currentState.title,
                    body = currentState.body,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    image = currentState.image,
                    color = currentState.color,
                    isPinned = currentState.isPinned,
                    themeId = currentState.themeId
                    // ...set other fields from uiState...
                )
                val tags = currentState.tags
                notesRepository.insertNoteWithTags(newNote, tags)
            }

            // Here you would typically navigate back after saving
        }
    }

    fun onEditModeChanged(newEditingMode: Boolean) {
        updateState(uiState.value.copy(isEditing = newEditingMode))
    }

    fun onTitleChange(newTitle: String) {
        updateState(uiState.value.copy(title = newTitle))
    }

    fun onBodyChange(newBody: String) {
        updateState(uiState.value.copy(body = newBody))
    }

    fun onHeadingChange(newHeading: String) {
        updateState(uiState.value.copy(heading = newHeading))
    }

    fun onImageChange(newImage: String?) {
        updateState(uiState.value.copy(image = newImage))
    }

    fun onColorChange(newColor: String?) {
        updateState(uiState.value.copy(color = newColor))
    }

    fun onIsPinnedChange(newIsPinned: Boolean) {
        updateState(uiState.value.copy(isPinned = newIsPinned))
    }

    fun onThemeChange(newThemeId: Int) {
        updateState(uiState.value.copy(themeId = newThemeId))
    }

    fun onTagsChange(newTags: List<Tag>) {
        updateState(uiState.value.copy(tags = newTags))
    }

    private fun updateState(newState: DetailedNoteUiState) {

        if (!newState.isEditing) {
            newState.isEditing = true
        }
        _uiState.update { newState }
    }
}