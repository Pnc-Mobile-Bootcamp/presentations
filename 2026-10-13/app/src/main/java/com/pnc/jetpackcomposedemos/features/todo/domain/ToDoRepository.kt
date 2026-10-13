package com.pnc.jetpackcomposedemos.features.todo.domain

interface ToDoRepository {

    suspend fun getToDos(): List<ToDo>
    suspend fun getToDo(id: Int): ToDo?
    suspend fun insert(todo: ToDo)
    suspend fun update(todo: ToDo)

}


