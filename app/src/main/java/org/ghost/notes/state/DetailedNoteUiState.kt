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

    // --- UI Operational State ---
    // These fields control the screen's behavior and appearance.
    val isLoading: Boolean = true,
    val isEditing: Boolean = false,
    val error: String? = null,

    // --- Original Note Reference ---
    // A copy of the original note from the database. Useful for
    // checking if changes have been made or for accessing non-editable data.
    val originalNote: Note? = null,
    val originalTags: List<Tag> = emptyList()

)