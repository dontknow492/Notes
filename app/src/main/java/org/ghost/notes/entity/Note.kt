package org.ghost.notes.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notes",
    indices = [
        Index(value = ["created_at"]), // For sorting by date
        Index(value = ["title"]),            // For sorting by title
        Index(value = ["heading"]),          // For sorting by heading
        Index(value = ["updated_at"])
    ]
)
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val heading: String,
    val title: String?,
    val body: String?,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    val image: String?,
    val color: String?,
    val isPinned: Boolean = false,
    @ColumnInfo(name = "theme_id") val themeId: Int
)
