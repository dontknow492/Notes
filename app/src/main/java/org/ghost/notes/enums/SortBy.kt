package org.ghost.notes.enums

enum class SortBy(val value: String) {
    TITLE("title"),
    HEADING("heading"),
    UPDATED_AT("updated_at"),
    CREATED_AT("created_at");

    companion object{
        fun fromValue(value: String): SortBy {
            return entries.first { it.value.equals(value, ignoreCase = true) }
        }
    }
}