package org.ghost.notes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.ghost.notes.dao.NotesDao
import org.ghost.notes.entity.Note
import org.ghost.notes.entity.NoteTags
import org.ghost.notes.entity.Tag

@Database(entities = [Note::class, Tag::class, NoteTags::class], version = 1)
abstract class NotesDatabase: RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object{
        private var INSTANCE: NotesDatabase? = null
        fun getInstance(context: Context): NotesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    "notes_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}