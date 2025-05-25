package com.example.androidproject.ui

import android.widget.DatePicker
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    selectedDate: Date?,
    onDateSelected: (Date) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()

    selectedDate?.let { calendar.time = it }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = selectedDate?.toString() ?: "No date selected",
            modifier = Modifier.weight(1f)
        )
        Button(onClick = { showDatePicker = true }) {
            Text("Choose Date")
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                        onDateSelected(calendar.time)
                        showDatePicker = false
                    })
                { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }
                ) { Text("Cancel") }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(
                    initialSelectedDateMillis = calendar.timeInMillis
                ),
                onDateSelected = { millis: Long ->
                    calendar.timeInMillis = millis
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DateSelectorPreview() {
    DateSelector(
        selectedDate = Calendar.getInstance().time,
        onDateSelected = {}
    )
}
