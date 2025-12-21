package com.example.androidproject.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.androidproject.data.FileNotebook
import com.example.androidproject.domain.Note

class NotesViewModel(private val context: Context) : ViewModel() {
    private val notebook = FileNotebook(context)

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            notebook.loadFromFile()
            _notes.value = notebook.notes
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            notebook.addNote(note)
            _notes.value = notebook.notes
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            notebook.updateNote(note)
            _notes.value = notebook.notes
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            notebook.removeNote(noteId)
            _notes.value = notebook.notes
        }
    }

    fun getNote(noteId: String): Note? {
        return notebook.getNote(noteId)
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel(context) as T
        }
    }
}