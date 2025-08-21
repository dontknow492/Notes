package org.ghost.notes.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Transaction
import org.ghost.notes.dao.NotesDao
import org.ghost.notes.entity.Note
import org.ghost.notes.entity.NoteTags
import org.ghost.notes.entity.Tag
import org.ghost.notes.enums.SortBy
import org.ghost.notes.enums.SortOrder
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val notesDao: NotesDao
) {
    fun filterNotes(
        query: String? = null,
        tagId: Int? = null,
        sortOrder: SortOrder = SortOrder.DESCENDING,
        sortBy: SortBy = SortBy.CREATED_AT
    ) = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { notesDao.filterNotes(
            query,
            tagId,
            sortOrder.value,
            sortBy.value
        ) }
    ).flow

    fun getDetailedNote(noteId: Int) = notesDao.getDetailedNote(noteId)

    @Transaction
    suspend fun insertNoteWithTags(note: Note, tags: List<Tag>) {
        // Step 1: Insert the note and get its new ID
        val noteId = notesDao.insertNote(note)

        // Step 2: Insert the tags and get their new IDs
        // (A more complex implementation would check for existing tags)
        val tagIds = notesDao.insertTags(tags)

        // Step 3: Create the links in the join table
        val noteTagRelations = tagIds.map { tagId ->
            NoteTags(noteId = noteId.toInt(), tagId = tagId.toInt())
        }
        notesDao.insertNoteTags(noteTagRelations)
    }

    @Transaction
    suspend fun updateNoteWithTags(note: Note, tags: List<Tag>) {
        // Step 1: Update the main Note object.
        notesDao.updateNote(note)

        // Step 2: Clear all existing tag relationships for this note.
        notesDao.clearTagsForNote(note.id)

        // Step 3: Create the new relationships in the join table.
        // (Assuming the Tag entities themselves already exist or are handled separately)
        if (tags.isNotEmpty()) {
            val noteTagRelations = tags.map { tag ->
                NoteTags(noteId = note.id, tagId = tag.id)
            }
            notesDao.insertNoteTags(noteTagRelations)
        }
    }


    suspend fun updateNote(note: Note) {
        notesDao.updateNote(note)
    }

    suspend fun deleteNote(note: Note) {
        notesDao.deleteNote(note)
    }



}