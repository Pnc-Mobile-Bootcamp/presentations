package com.pnc.jetpackcomposedemos.features.todo.presentation

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pnc.jetpackcomposedemos.features.todo.domain.ToDo

@Composable
fun ToDoCard(
    toDo: ToDo,
    onDoubleTap: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onDoubleClick = onDoubleTap
            )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (toDo.completed) {
                Icon(Icons.Default.Check, "Completed",
                    tint = MaterialTheme.colorScheme.primary)
            } else {
                Icon(Icons.Filled.CropSquare, "Not completed",
                    tint = MaterialTheme.colorScheme.tertiary)
            }
            Text(toDo.title)
        }
    }
}


