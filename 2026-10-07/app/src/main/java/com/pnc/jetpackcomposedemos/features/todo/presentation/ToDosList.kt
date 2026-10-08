package com.pnc.jetpackcomposedemos.features.todo.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pnc.jetpackcomposedemos.features.todo.domain.ToDo

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ToDosList(
    toDos: List<ToDo>,
    useTwoPaneLayout: Boolean,
    onAdd: (String) -> Unit,
    onToggleCompleted: (ToDo) -> Unit
) {
    var showDrawer by remember {
        mutableStateOf(false)
    }
    var newText by remember {
        mutableStateOf("")
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDrawer = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add a new to-do item"
                )
            }
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(if (useTwoPaneLayout) 2 else 1),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(toDos) { toDo: ToDo ->
                ToDoCard(
                    toDo = toDo,
                    onDoubleTap = {
                        onToggleCompleted(toDo)
                    }
                )
            }
        }

        if (showDrawer) {
            ModalBottomSheet(
                onDismissRequest = {
                    showDrawer = false
                }
            ) {
                TextField(
                    value = newText,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onValueChange = { newText = it },
                    label = { Text("New to-do") }
                )

                Button(
                    onClick = {
                        onAdd(newText)
                        showDrawer = false
                        newText = ""
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                ) {
                    Text("Add")
                }
            }
        }
    }
}