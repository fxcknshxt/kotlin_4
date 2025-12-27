package com.example.androidproject.data.local

import android.content.Context
import com.example.androidproject.data.FileNotebook
import com.example.androidproject.domain.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalNoteDataSource(private val context: Context) {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val fileNotebook = FileNotebook(context)

    init {
        loadInitialNotes()
    }

    private fun loadInitialNotes() {
        _notes.value = fileNotebook.notes
    }

    fun getAllNotes(): Flow<List<Note>> = notes

    fun getNoteById(uid: String): Note? {
        return _notes.value.find { it.uid == uid }
    }

    suspend fun saveNotes(notes: List<Note>) {
        _notes.value = notes
        saveToFile(notes)
    }

    suspend fun saveNote(note: Note) {
        val currentNotes = _notes.value.toMutableList()
        val index = currentNotes.indexOfFirst { it.uid == note.uid }

        if (index != -1) {
            currentNotes[index] = note
        } else {
            currentNotes.add(note)
        }

        _notes.value = currentNotes
        saveToFile(currentNotes)
    }

    suspend fun deleteNote(uid: String) {
        val newNotes = _notes.value.filter { it.uid != uid }
        _notes.value = newNotes
        saveToFile(newNotes)
    }

    private suspend fun saveToFile(notes: List<Note>) {
        _notes.value.forEach { note ->
            fileNotebook.removeNote(note.uid)
        }
        notes.forEach { note ->
            fileNotebook.addNote(note)
        }
    }

    suspend fun refreshFromCache() {
        fileNotebook.loadFromFile()
        _notes.value = fileNotebook.notes
    }
}