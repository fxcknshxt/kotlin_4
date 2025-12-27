package com.example.androidproject.data.remote

import com.example.androidproject.domain.Note
import kotlinx.coroutines.delay

class RemoteNoteDataSource {
    private val networkDelay = 1000L

    private var backendNotes = mutableListOf<Note>()

    suspend fun getAllNotes(): List<Note> {
        delay(networkDelay)
        return backendNotes.toList()
    }

    suspend fun getNoteById(uid: String): Note? {
        delay(networkDelay)
        return backendNotes.find { it.uid == uid }
    }

    suspend fun saveNote(note: Note): Note {
        delay(networkDelay)
        val index = backendNotes.indexOfFirst { it.uid == note.uid }
        if (index != -1) {
            backendNotes[index] = note
        } else {
            backendNotes.add(note)
        }
        return note
    }

    suspend fun deleteNote(uid: String): Boolean {
        delay(networkDelay)
        return backendNotes.removeIf { it.uid == uid }
    }
}