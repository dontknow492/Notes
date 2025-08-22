package org.ghost.notes.viewModels

import android.util.Log
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
    private var noteId: Int = savedStateHandle.get<Int>("noteId") ?: -1

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
                                createdAt = noteWithTags.note.createdAt,
                                updatedAt = noteWithTags.note.updatedAt,
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
        viewModelScope.launch {

            val currentState = _uiState.value

            Log.d("DetailedNoteViewModel", "save request: $currentState")

            if (currentState.heading.isEmpty()) {
                updateState(
                    currentState.copy(
                        validationState = currentState.validationState.copy(
                            isHeadingEmpty = true
                        )
                    )
                )
                return@launch
            }
            if (currentState.heading.length > 100) {
                updateState(
                    currentState.copy(
                        validationState = currentState.validationState.copy(
                            isHeadingTooLong = true
                        )
                    )
                )
                return@launch
            }
            if (currentState.title.length > 100) {
                updateState(
                    currentState.copy(
                        validationState = currentState.validationState.copy(
                            isTitleTooLong = true
                        )
                    )
                )
                return@launch
            }

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
                try {
                    notesRepository.updateNoteWithTags(updatedNote, currentState.tags)
                } catch (e: Exception) {
                    Log.d("DetailedNoteViewModel", "Error updating note with tags: $e")
                    updateState(
                        currentState.copy(
                            isLoading = false,
                            error = e.message,
                            generalError = e.message
                        )
                    )
                    return@launch
                }

                Log.d("DetailedNoteViewModel", "Note updated successfully: ${updatedNote.id}")

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
                try {
                    noteId = notesRepository.insertNoteWithTags(newNote, tags).toInt()
                } catch (e: Exception) {
                    Log.d("DetailedNoteViewModel", "Error inserting note with tags: $e")
                    updateState(
                        currentState.copy(
                            isLoading = false,
                            error = e.message,
                            generalError = e.message
                        )
                    )
                    return@launch
                }
                Log.d("DetailedNoteViewModel", "Note created successfully: $noteId")

                updateState(
                    currentState.copy(
                        originalNote = newNote.copy(id = noteId.toInt()),
                        isEditing = false,
                        isLoading = false,
                    )
                )
            }

            onEditModeChanged(false)

            Log.d("DetailedNoteViewModel", "saveNote: Done, isEditing: ${uiState.value.isEditing}")

            // Here you would typically navigate back after saving
        }
    }


    fun onEditModeChanged(newEditingMode: Boolean) {
        _uiState.update {
            uiState.value.copy(
                isEditing = newEditingMode
            )
        }
    }

    fun onTitleChange(newTitle: String) {
        updateState(
            uiState.value.copy(
                title = newTitle,
                validationState = uiState.value.validationState.copy(
                    isTitleEmpty = false,
                    isTitleTooLong = false
                )
            )
        )
    }

    fun onBodyChange(newBody: String) {
        updateState(uiState.value.copy(body = newBody))
    }

    fun onHeadingChange(newHeading: String) {
        updateState(
            uiState.value.copy(
                heading = newHeading,
                validationState = uiState.value.validationState.copy(
                    isHeadingEmpty = false,
                    isHeadingTooLong = false
                )
            )
        )
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

    fun addTag(newTag: Tag) {
        val newTags = uiState.value.tags.toMutableList()
        newTags.add(newTag)
        onTagsChange(newTags)
    }

    fun addTag(newTag: String) {
        val newTags = uiState.value.tags.toMutableList()
        newTags.add(Tag(name = newTag))
        onTagsChange(newTags)
    }

    private fun updateState(newState: DetailedNoteUiState) {

        if (!newState.isEditing) {
            newState.isEditing = true
        }
        _uiState.update { newState }
    }
}