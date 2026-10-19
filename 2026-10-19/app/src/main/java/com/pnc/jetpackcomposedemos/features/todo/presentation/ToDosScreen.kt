package com.pnc.jetpackcomposedemos.features.todo.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pnc.jetpackcomposedemos.core.LoadableState
import com.pnc.jetpackcomposedemos.features.todo.domain.ToDo

@Composable
fun ToDosScreen(
    useTwoPaneLayout: Boolean,
    viewModel: ToDosViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadToDos()
    }

    when (uiState) {
        is LoadableState.Loading -> CircularProgressIndicator()
        is LoadableState.Empty -> Text("No to-dos")
        is LoadableState.Error -> {
            val errorMessage = (uiState as LoadableState.Error).message
            Text(
                "Error: $errorMessage",
                color = MaterialTheme.colorScheme.error
            )
        }
        is LoadableState.Success -> {
            val toDos = (uiState as LoadableState.Success).data
            ToDosList(
                toDos = toDos,
                useTwoPaneLayout = useTwoPaneLayout,
                onAdd = { item: String ->
                    viewModel.createToDo(item)
                },
                onToggleCompleted = { item: ToDo ->
                    viewModel.toggleCompleted(item)
                }
            )
        }
    }

}


