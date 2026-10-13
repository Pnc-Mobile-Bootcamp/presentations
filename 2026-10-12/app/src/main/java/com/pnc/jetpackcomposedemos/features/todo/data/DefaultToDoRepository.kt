package com.pnc.jetpackcomposedemos.features.todo.data

import com.pnc.jetpackcomposedemos.features.todo.domain.ToDo
import com.pnc.jetpackcomposedemos.features.todo.domain.ToDoRepository
import javax.inject.Inject

class DefaultToDoRepository @Inject constructor(
    private val toDoDao: ToDoDao
): ToDoRepository {

    override suspend fun getToDos(): List<ToDo> {
        return toDoDao.getToDos().map {
            ToDoMapper.toDomain(it)
        }
    }

    override suspend fun getToDo(id: Int): ToDo? {
        return toDoDao.getToDo(id)?.let {
            ToDoMapper.toDomain(it)
        }
    }

    override suspend fun insert(todo: ToDo) {
        toDoDao.insertToDo(ToDoMapper.toEntity(todo))
    }

    override suspend fun update(todo: ToDo) {
        toDoDao.updateToDo(ToDoMapper.toEntity(todo))
    }

}
