package com.example.androidproject.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import com.example.androidproject.domain.Note
import com.example.androidproject.data.FileNotebook
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Scaffold
import com.example.androidproject.ui.theme.AndroidProjectTheme

class MainActivity : ComponentActivity() {
    private val fileNotebook: FileNotebook = FileNotebook(context = this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fileNotebook.loadFromFile()

        val newNote = Note(title = "New Title", content = "New Content")
        fileNotebook.addNote(newNote)

        fileNotebook.saveToFile()

        fileNotebook.scheduleNoteDeletion("some-uid", 60000L)

        setContent {
            AndroidProjectTheme {
                NoteListScreen(notes = fileNotebook.getNotes(), fileNotebook = fileNotebook)
            }
        }
    }
}


@Composable
fun NoteListScreen(notes: List<Note>, fileNotebook: FileNotebook) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Button(
                onClick = {
                    val newNote = Note(title = "New Title", content = "New Content")
                    fileNotebook.addNote(newNote)
                    fileNotebook.saveToFile()
                }
            ) {
                Text("Add New Note")
            }

            LazyColumn {
                items(notes) { note ->
                    Text(note.title)
                    Button(onClick = {}) {
                        Text("Edit")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGreeting() {
    val fileNotebook = FileNotebook(context = LocalContext.current)

    NoteListScreen(
        notes = listOf(
            Note(title = "Example_Title", content = "Example_Content")
        ),
        fileNotebook = fileNotebook
    )
}

