package org.ghost.notes.state

import org.ghost.notes.enums.SortBy
import org.ghost.notes.enums.SortOrder

data class NotesFilter(
    val query: String? = null,
    val tagId: Int? = null,
    val sortOrder: SortOrder = SortOrder.DESCENDING,
    val sortBy: SortBy = SortBy.CREATED_AT
)