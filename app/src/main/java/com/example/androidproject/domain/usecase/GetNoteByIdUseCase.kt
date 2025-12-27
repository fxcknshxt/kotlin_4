package com.example.androidproject.domain.usecase

import com.example.androidproject.domain.Note
import com.example.androidproject.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(uid: String): Flow<Note?> {
        return repository.getNoteById(uid)
    }
}