package com.example.androidproject.domain.usecase

import com.example.androidproject.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(uid: String): Boolean {
        return repository.deleteNote(uid)
    }
}