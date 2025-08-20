package org.ghost.notes.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class NoteWithTags(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "id", // From the Note class
        entity = Tag::class, // The entity we want to get
        entityColumn = "id", // From the Tag class
        associateBy = Junction(
            value = NoteTags::class, // The join table
            parentColumn = "note_id", // Column in join table that points to Note
            entityColumn = "tag_id" // Column in join table that points to Tag
        )
    )
    val tags: List<Tag>
)