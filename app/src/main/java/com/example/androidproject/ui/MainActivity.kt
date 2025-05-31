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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType


class MainActivity : ComponentActivity() {
    private lateinit var fileNotebook: FileNotebook

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fileNotebook = FileNotebook(this)
        fileNotebook.loadFromFile()

        setContent {
            AndroidProjectTheme {

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "noteList") {
                    composable("noteList") {
                        NoteListScreen(notes = fileNotebook.getNotes(), fileNotebook = fileNotebook, navController = navController)
                    }
                    composable(
                        route = "editNote/{noteId}",
                        arguments = listOf(navArgument("noteId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getString("noteId")
                        val note = fileNotebook.getNotes().find { it.uid == noteId }
                        note?.let {
                            EditNoteScreen(
                                state = NoteState(note = it),
                                onSave = { updatedNote ->
                                    fileNotebook.removeNote(it.uid)
                                    fileNotebook.addNote(updatedNote)
                                    fileNotebook.saveToFile()
                                    navController.navigateUp()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun NoteListScreen(notes: List<Note>, fileNotebook: FileNotebook, navController: androidx.navigation.NavController) {
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
                    Button(onClick = {
                        navController.navigate("editNote/${note.uid}")
                    }) {
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
    val navController = rememberNavController()

    NoteListScreen(
        notes = listOf(
            Note(title = "Example_Title", content = "Example_Content")
        ),
        fileNotebook = fileNotebook,
        navController = navController
    )
}

