package com.example.androidproject.presentation.navigation

object Routes {
    const val NOTES_LIST = "notes_list"
    const val EDIT_NOTE = "edit_note"
    const val ADD_NOTE = "add_note"
    const val EDIT_NOTE_WITH_ID = "$EDIT_NOTE/{noteId}"

    fun editNoteWithId(noteId: String): String {
        return "$EDIT_NOTE/$noteId"
    }
}