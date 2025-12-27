package com.example.androidproject.domain.repository

import com.example.androidproject.domain.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    fun getNoteById(uid: String): Flow<Note?>

    suspend fun saveNotesToCache(notes: List<Note>)
    suspend fun saveNoteToCache(note: Note)
    suspend fun deleteNoteFromCache(uid: String)

    suspend fun loadNotesFromBackend(): List<Note>
    suspend fun saveNoteToBackend(note: Note): Note
    suspend fun deleteNoteFromBackend(uid: String): Boolean

    suspend fun addNote(note: Note): Note
    suspend fun updateNote(note: Note): Note
    suspend fun deleteNote(uid: String): Boolean
}