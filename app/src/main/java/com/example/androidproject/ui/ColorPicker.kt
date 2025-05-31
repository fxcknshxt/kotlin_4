package com.example.androidproject.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Check
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.unit.dp

@Composable
fun ColorPicker(
    selectedColor: Int,
    onColorSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        Color.Red,
        Color.Yellow,
        Color.Green,
        Color.Blue,
        Color.Magenta
    )

    Column(modifier) {
        Text("Choose a color", style = MaterialTheme.typography.labelMedium)

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            colors.forEach { (color, _) ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(color.toInt()))
                        .border(
                            width = if (color.toInt() == selectedColor) 2.dp else 1.dp,
                            color = if (color.toInt() == selectedColor) Color.Black else Color.Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable { onColorSelected(color.toInt()) }
                ) {
                    if (color.toInt() == selectedColor) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            var showCustomPicker by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { showCustomPicker = true }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = { showCustomPicker = true }
                        )
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = "Custom Color",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            if (showCustomPicker) {
                CustomColorPicker(
                    initialColor = selectedColor,
                    onColorSelected = {
                        onColorSelected(it)
                        showCustomPicker = false
                    },
                    onDismiss = { showCustomPicker = false }
                )
            }
        }
    }
}

@Composable
private fun CustomColorPicker(
    initialColor: Int,
    onColorSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Column {
        Text("Select Custom Color")
        ColorPicker(selectedColor = initialColor, onColorSelected = onColorSelected)
        Button(onClick = { onDismiss() }) {
            Text("Dismiss")
        }
    }
}
