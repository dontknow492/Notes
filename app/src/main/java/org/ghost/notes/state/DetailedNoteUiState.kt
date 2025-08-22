package org.ghost.notes.state

import org.ghost.notes.entity.Note
import org.ghost.notes.entity.Tag

data class DetailedNoteUiState(
    // --- Editable Note Content ---
    // These fields act as a temporary draft for user input.
    val heading: String = "",
    val title: String = "",
    val body: String = "",
    val image: String? = null,
    val color: String? = null,
    val isPinned: Boolean = false,
    val themeId: Int = 0, // Assuming 0 is a default theme ID
    val tags: List<Tag> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),

    // --- UI Operational State ---
    // These fields control the screen's behavior and appearance.
    val isLoading: Boolean = true,
    var isEditing: Boolean = false,
    val error: String? = null,
    val generalError: String? = null, // For network/DB errors
    val validationState: NoteValidationState = NoteValidationState(), // For field errors

    // --- Original Note Reference ---
    // A copy of the original note from the database. Useful for
    // checking if changes have been made or for accessing non-editable data.
    val originalNote: Note? = null,
    val originalTags: List<Tag> = emptyList()

)

data class NoteValidationState(
    val isHeadingEmpty: Boolean = false,
    val isTitleEmpty: Boolean = false,
    val isBodyEmpty: Boolean = false,
    val isTitleTooLong: Boolean = false,
    val isHeadingTooLong: Boolean = false
    // You could add other rules here later, e.g., isTitleTooLong: Boolean = false
)