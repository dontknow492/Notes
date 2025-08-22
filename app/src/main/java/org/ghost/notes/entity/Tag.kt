package org.ghost.notes.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tags",
    indices = [Index(
        value = ["name"],
        unique = true
    )] // Speeds up search and ensures no duplicate tags
)
data class Tag(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
)
