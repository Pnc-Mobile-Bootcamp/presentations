package com.pnc.jetpackcomposedemos.features.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class ToDoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val completed: Boolean
)

