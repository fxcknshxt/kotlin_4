package com.example.androidproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.domain.Note
import com.example.androidproject.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            getAllNotesUseCase()
                .onStart { _isLoading.value = true }
                .catch { error ->
                    _error.value = "Ошибка загрузки: ${error.message}"
                    _isLoading.value = false
                }
                .collect { notes ->
                    _notes.value = notes
                    _isLoading.value = false
                }
        }
    }

    fun getNote(uid: String): Flow<Note?> {
        return getNoteByIdUseCase(uid)
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                addNoteUseCase(note)
                // Данные автоматически обновятся через Flow
            } catch (e: Exception) {
                _error.value = "Ошибка добавления: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                updateNoteUseCase(note)
            } catch (e: Exception) {
                _error.value = "Ошибка обновления: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteNote(uid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                deleteNoteUseCase(uid)
            } catch (e: Exception) {
                _error.value = "Ошибка удаления: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}