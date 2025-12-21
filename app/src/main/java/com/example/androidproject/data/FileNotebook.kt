package com.example.androidproject.data

import android.content.Context
import org.json.JSONArray
import org.slf4j.LoggerFactory
import java.io.File
import com.example.androidproject.domain.Note

class FileNotebook(private val context: Context) {
    private val logger = LoggerFactory.getLogger(FileNotebook::class.java)
    private var _notes: List<Note> = emptyList()

    val notes: List<Note>
        get() = _notes

    private val notesFile by lazy {
        File(context.filesDir, "notes.json").apply {
            parentFile?.mkdirs()
        }
    }

    init {
        logger.info("FileNotebook initialized with path: ${context.filesDir}")
        loadFromFile()
    }

    fun addNote(note: Note): Boolean {
        return try {
            _notes = _notes + note
            saveToFile()
            logger.info("Note added: ${note.title}")
            true
        } catch (e: Exception) {
            logger.error("Error adding note", e)
            false
        }
    }

    fun removeNote(uid: String): Boolean {
        val noteToRemove = _notes.find { it.uid == uid }
        return if (noteToRemove != null) {
            _notes = _notes.filter { it.uid != uid }
            saveToFile()
            logger.info("Note removed: ${noteToRemove.title}")
            true
        } else {
            logger.warn("Note with uid $uid not found")
            false
        }
    }

    fun updateNote(updatedNote: Note): Boolean {
        val index = _notes.indexOfFirst { it.uid == updatedNote.uid }
        return if (index != -1) {
            val newList = _notes.toMutableList()
            newList[index] = updatedNote
            _notes = newList
            saveToFile()
            logger.info("Note updated: ${updatedNote.title}")
            true
        } else {
            logger.warn("Note with uid ${updatedNote.uid} not found for update")
            false
        }
    }

    fun getNote(uid: String): Note? {
        return _notes.find { it.uid == uid }
    }

    private fun saveToFile(): Boolean {
        return try {
            val jsonArray = JSONArray().apply {
                _notes.forEach { note ->
                    put(note.toJson())
                }
            }
            notesFile.writeText(jsonArray.toString())
            logger.info("Notes saved to file. Total notes: ${_notes.size}")
            true
        } catch (e: Exception) {
            logger.error("Error saving notes to file", e)
            false
        }
    }

    fun loadFromFile(): Boolean {
        return try {
            if (!notesFile.exists()) {
                logger.info("No notes file found at ${notesFile.path}")
                return false
            }

            val jsonText = notesFile.readText()
            if (jsonText.isBlank()) {
                logger.info("Notes file is empty")
                return false
            }

            val jsonArray = JSONArray(jsonText)
            val loadedNotes = mutableListOf<Note>()

            for (i in 0 until jsonArray.length()) {
                Note.parse(jsonArray.getJSONObject(i))?.let { note ->
                    loadedNotes.add(note)
                }
            }

            _notes = loadedNotes
            logger.info("Notes loaded from file. Total notes: ${_notes.size}")
            true
        } catch (e: Exception) {
            logger.error("Error loading notes from file", e)
            false
        }
    }
}