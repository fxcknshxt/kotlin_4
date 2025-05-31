package com.example.androidproject.ui

import com.example.androidproject.domain.Note
import com.example.androidproject.domain.Importance
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    state: NoteState,
    onSave: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf(state.note.title) }
    var content by remember { mutableStateOf(state.note.content) }
    var color by remember { mutableStateOf(state.note.color) }
    var importance by remember { mutableStateOf(state.note.importance) }

    Column(modifier = modifier.padding(16.dp).fillMaxSize()) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title of the note") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content of the note") },
            modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp)
        )

        ColorPicker(selectedColor = color, onColorSelected = { color = it })

        ImportanceSelector(selectedImportance = importance, onImportanceSelected = { importance = it })

        Button(
            onClick = {
                val updatedNote = state.note.copy(title = title, content = content, color = color, importance = importance)
                onSave(updatedNote)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save")
        }
    }
}

@Composable
private fun ImportanceSelector(
    selectedImportance: Importance,
    onImportanceSelected: (Importance) -> Unit
) {
    Column {
        Text("Level of importance", style = MaterialTheme.typography.labelMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Importance.values().forEach { importance ->
                FilterChip(
                    selected = importance == selectedImportance,
                    onClick = { onImportanceSelected(importance) },
                    label = { Text(importance.name) }
                )
            }
        }
    }
}

data class NoteState(val note: Note)