package com.example.androidproject.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidproject.domain.Importance
import com.example.androidproject.domain.Note
import com.example.androidproject.presentation.components.ColorPicker
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    note: Note?,
    onSave: (Note) -> Unit,
    onCancel: () -> Unit
) {

    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var color by remember { mutableStateOf(note?.color ?: android.graphics.Color.WHITE) }
    var importance by remember { mutableStateOf(note?.importance ?: Importance.NORMAL) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (note == null) "Новая заметка" else "Редактировать")
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val newNote = if (note == null) {

                                Note(
                                    uid = UUID.randomUUID().toString(),
                                    title = title,
                                    content = content,
                                    color = color,
                                    importance = importance
                                )
                            } else {

                                note.copy(
                                    title = title,
                                    content = content,
                                    color = color,
                                    importance = importance
                                )
                            }
                            onSave(newNote)
                        },
                        enabled = title.isNotBlank()
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Сохранить")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Заголовок") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = title.isBlank()
            )

            if (title.isBlank()) {
                Text(
                    text = "Заголовок не может быть пустым",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            ImportanceSelector(
                selectedImportance = importance,
                onImportanceSelected = { importance = it }
            )

            Text(
                text = "Цвет заметки",
                style = MaterialTheme.typography.labelMedium
            )
            ColorPicker(
                selectedColor = color,
                onColorSelected = { color = it }
            )

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Содержимое заметки") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp),
                singleLine = false,
                maxLines = 10
            )

            Button(
                onClick = {
                    val newNote = if (note == null) {
                        Note(
                            uid = UUID.randomUUID().toString(),
                            title = title,
                            content = content,
                            color = color,
                            importance = importance
                        )
                    } else {
                        note.copy(
                            title = title,
                            content = content,
                            color = color,
                            importance = importance
                        )
                    }
                    onSave(newNote)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank()
            ) {
                Text("Сохранить заметку")
            }

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Отмена")
            }
        }
    }
}

@Composable
private fun ImportanceSelector(
    selectedImportance: Importance,
    onImportanceSelected: (Importance) -> Unit
) {
    Column {
        Text(
            text = "Важность",
            style = MaterialTheme.typography.labelMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Importance.values().forEach { importance ->
                FilterChip(
                    selected = importance == selectedImportance,
                    onClick = { onImportanceSelected(importance) },
                    label = {
                        Text(
                            when (importance) {
                                Importance.LOW -> "Неважная"
                                Importance.NORMAL -> "Обычная"
                                Importance.HIGH -> "Важная"
                            }
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}