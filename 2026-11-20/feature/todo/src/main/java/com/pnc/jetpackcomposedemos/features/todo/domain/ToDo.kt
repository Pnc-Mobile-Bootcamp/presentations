package com.pnc.jetpackcomposedemos.features.todo.domain

data class ToDo(
    val id: Int,
    val title: String,
    val completed: Boolean,
    val position: Int = 0
)


