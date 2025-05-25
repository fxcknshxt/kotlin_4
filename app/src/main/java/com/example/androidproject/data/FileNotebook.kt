package com.example.androidproject.data

import android.content.Context
import org.json.JSONArray
import org.slf4j.LoggerFactory
import java.io.File
import java.util.Timer
import java.util.TimerTask
import com.example.androidproject.domain.Note


class FileNotebook(private val context: Context) {
    private val logger = LoggerFactory.getLogger(FileNotebook::class.java)
    private val notes = mutableListOf<Note>()
    private val notesFile by lazy { File(context.filesDir, "notes.json") }

    init {
        logger.info("FileNotebook initialized with path: ${context.filesDir}")
    }

    fun getNotes(): List<Note> {
        return notes
    }

    fun addNote(note: Note) {
        notes.add(note)
        logger.debug("Note added: ${note.title}")
    }

    fun removeNote(uid: String): Boolean {
        val result = notes.removeIf { it.uid == uid }
        if (result) {
            logger.debug("Note removed with uid: $uid")
        } else {
            logger.warn("Note with uid $uid not found for removal")
        }
        return result
    }

    fun saveToFile() {
        try {
            val jsonArray = JSONArray().apply {
                notes.forEach { put(it.toJson()) }
            }
            notesFile.writeText(jsonArray.toString())
            logger.info("Notes saved to file. Total notes: ${notes.size}")
        } catch (e: Exception) {
            logger.error("Error saving notes to file", e)
        }
    }

    fun loadFromFile() {
        try {
            if (!notesFile.exists()) {
                logger.info("No notes file found at ${notesFile.path}")
                return
            }

            notes.clear()
            JSONArray(notesFile.readText()).let { jsonArray ->
                for (i in 0 until jsonArray.length()) {
                    Note.parse(jsonArray.getJSONObject(i))?.let { note ->
                        notes.add(note)
                    }
                }
            }
            logger.info("Notes loaded from file. Total notes: ${notes.size}")
        } catch (e: Exception) {
            logger.error("Error loading notes from file", e)
        }
    }

    fun scheduleNoteDeletion(uid: String, delayMillis: Long = 60000) {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                Thread {
                    removeNote(uid)
                }.start()
            }
        }, delayMillis)
    }
}
