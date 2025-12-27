package com.example.androidproject.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.data.local.LocalNoteDataSource
import com.example.androidproject.data.remote.RemoteNoteDataSource
import com.example.androidproject.data.repository.NoteRepositoryImpl
import com.example.androidproject.domain.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SimpleNotesViewModel(context: Context) : ViewModel() {
    private val localDataSource = LocalNoteDataSource(context)
    private val remoteDataSource = RemoteNoteDataSource()
    private val repository = NoteRepositoryImpl(localDataSource, remoteDataSource)

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _notes.value = localDataSource.notes.value

                syncWithBackend()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun syncWithBackend() {
        viewModelScope.launch {
            try {
                val backendNotes = repository.loadNotesFromBackend()
                if (backendNotes.isNotEmpty()) {
                    localDataSource.saveNotes(backendNotes)
                    _notes.value = backendNotes
                }
            } catch (e: Exception) {
                println("Ошибка синхронизации с бэкендом: ${e.message}")
            }
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                localDataSource.saveNote(note)
                _notes.value = localDataSource.notes.value

                viewModelScope.launch {
                    try {
                        repository.addNote(note)
                    } catch (e: Exception) {
                        println("Ошибка отправки в бэкенд: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("Ошибка добавления заметки: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                localDataSource.saveNote(note)
                _notes.value = localDataSource.notes.value

                viewModelScope.launch {
                    try {
                        repository.updateNote(note)
                    } catch (e: Exception) {
                        println("Ошибка синхронизации: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("Ошибка обновления: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteNote(uid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                localDataSource.deleteNote(uid)
                _notes.value = localDataSource.notes.value

                viewModelScope.launch {
                    try {
                        repository.deleteNote(uid)
                    } catch (e: Exception) {
                        println("Ошибка синхронизации: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("Ошибка удаления: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getNoteById(uid: String): Note? {
        return localDataSource.getNoteById(uid)
    }
}