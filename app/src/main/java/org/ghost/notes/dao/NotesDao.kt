package org.ghost.notes.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.ghost.notes.entity.Note
import org.ghost.notes.entity.NoteTags
import org.ghost.notes.entity.NoteWithTags
import org.ghost.notes.entity.Tag
import org.ghost.notes.enums.SortBy
import org.ghost.notes.enums.SortOrder

@Dao
interface NotesDao {
    @Transaction
    @Query(
        """
        SELECT notes.* FROM notes
        LEFT JOIN note_tags ON notes.id = note_tags.note_id
        WHERE
            (:query IS NULL OR notes.title LIKE '%' || :query || '%' OR notes.heading LIKE '%' || :query || '%')
            AND
            (:tagId IS NULL OR note_tags.tag_id = :tagId)
        GROUP BY notes.id
        ORDER BY
            -- This block handles ASCENDING order
            CASE WHEN :sortOrder = 'ASC' THEN
                CASE :sortBy
                    WHEN 'title' THEN notes.title
                    WHEN 'heading' THEN notes.heading
                    WHEN 'updated_at' THEN notes.updated_at
                    WHEN 'created_at' THEN notes.created_at
                    ELSE notes.id
                END
            END ASC,
    
            -- This block handles DESCENDING order
            CASE WHEN :sortOrder = 'DESC' THEN
                CASE :sortBy
                    WHEN 'title' THEN notes.title
                    WHEN 'heading' THEN notes.heading
                    WHEN 'updated_at' THEN notes.updated_at
                    WHEN 'created_at' THEN notes.created_at
                    ELSE notes.id
                END
            END DESC
    """
    )
    fun filterNotes(
        query: String?,
        tagId: Int?,
        sortOrder: String = SortOrder.DESCENDING.value,
        sortBy: String = SortBy.CREATED_AT.value
    ): PagingSource<Int, Note>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    fun getDetailedNote(noteId: Int): Flow<NoteWithTags?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: Tag): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTags(tag: List<Tag>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteTags(noteTags: NoteTags): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteTags(noteTags: List<NoteTags>) // Links them

    @Update
    suspend fun updateNote(note: Note)

    @Update
    suspend fun updateTags(tags: List<Tag>)

    @Query("DELETE FROM note_tags WHERE note_id = :noteId")
    suspend fun clearTagsForNote(noteId: Int)


    @Delete
    suspend fun deleteNote(note: Note)


}