package com.example.androidproject.domain.usecase

import com.example.androidproject.domain.Note
import com.example.androidproject.domain.repository.NoteRepository
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note): Note {
        return repository.addNote(note)
    }
}