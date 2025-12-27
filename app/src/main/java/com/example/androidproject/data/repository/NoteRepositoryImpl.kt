package com.example.androidproject.data.repository

import com.example.androidproject.data.local.LocalNoteDataSource
import com.example.androidproject.data.remote.RemoteNoteDataSource
import com.example.androidproject.domain.Note
import com.example.androidproject.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val localDataSource: LocalNoteDataSource,
    private val remoteDataSource: RemoteNoteDataSource
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return localDataSource.getAllNotes()
    }

    override fun getNoteById(uid: String): Flow<Note?> {
        // Возвращаем Flow<Note?>, преобразуя Flow<List<Note>>
        return localDataSource.getAllNotes().map { notesList ->
            notesList.find { it.uid == uid }
        }
    }

    override suspend fun saveNotesToCache(notes: List<Note>) {
        localDataSource.saveNotes(notes)
    }

    override suspend fun saveNoteToCache(note: Note) {
        localDataSource.saveNote(note)
    }

    override suspend fun deleteNoteFromCache(uid: String) {
        localDataSource.deleteNote(uid)
    }

    override suspend fun loadNotesFromBackend(): List<Note> {
        // Загружаем с бэкенда
        val notes = remoteDataSource.getAllNotes()
        // Сохраняем в кэш
        localDataSource.saveNotes(notes)
        return notes
    }

    override suspend fun saveNoteToBackend(note: Note): Note {
        // Отправляем на бэкенд
        val savedNote = remoteDataSource.saveNote(note)
        // Сохраняем в кэш
        localDataSource.saveNote(savedNote)
        return savedNote
    }

    override suspend fun deleteNoteFromBackend(uid: String): Boolean {
        // Удаляем с бэкенда
        val success = remoteDataSource.deleteNote(uid)
        if (success) {
            // Удаляем из кэша
            localDataSource.deleteNote(uid)
        }
        return success
    }

    override suspend fun addNote(note: Note): Note {
        // Сначала сохраняем локально для мгновенной обратной связи
        localDataSource.saveNote(note)
        // Пытаемся отправить на бэкенд
        return try {
            saveNoteToBackend(note)
        } catch (e: Exception) {
            // Если ошибка сети, оставляем только в кэше
            note
        }
    }

    override suspend fun updateNote(note: Note): Note {
        localDataSource.saveNote(note)
        return try {
            saveNoteToBackend(note)
        } catch (e: Exception) {
            note
        }
    }

    override suspend fun deleteNote(uid: String): Boolean {
        localDataSource.deleteNote(uid)
        return try {
            deleteNoteFromBackend(uid)
        } catch (e: Exception) {
            false
        }
    }
}