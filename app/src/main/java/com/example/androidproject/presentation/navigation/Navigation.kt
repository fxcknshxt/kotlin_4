package com.example.androidproject.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidproject.presentation.screens.EditNoteScreen
import com.example.androidproject.presentation.screens.NotesListScreen
import com.example.androidproject.presentation.viewmodel.NotesViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val viewModel: NotesViewModel = viewModel(
        factory = NotesViewModel.Factory(context)
    )

    NavHost(
        navController = navController,
        startDestination = Routes.NOTES_LIST
    ) {
        composable(Routes.NOTES_LIST) {
            NotesListScreen(
                onNoteClick = { noteId ->
                    navController.navigate(Routes.editNoteWithId(noteId))
                },
                onAddNoteClick = {
                    navController.navigate(Routes.ADD_NOTE)
                },
                viewModel = viewModel
            )
        }

        composable(
            route = Routes.EDIT_NOTE_WITH_ID,
            arguments = listOf(
                navArgument("noteId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            val note = noteId?.let { viewModel.getNote(it) }

            EditNoteScreen(
                note = note,
                onSave = { updatedNote ->
                    if (note != null) {
                        viewModel.updateNote(updatedNote)
                    } else {
                        viewModel.addNote(updatedNote)
                    }
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.ADD_NOTE) {
            EditNoteScreen(
                note = null,
                onSave = { newNote ->
                    viewModel.addNote(newNote)
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }
    }
}