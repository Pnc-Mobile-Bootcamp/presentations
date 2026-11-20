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
        // a new to-do (position = 0) goes at the end of the list
        // an updated to-do keeps the position it had
        val position = if (todo.position == 0) toDoDao.maxPosition() + 1 else todo.position
        toDoDao.insertToDo(ToDoMapper.toEntity(todo.copy(position = position)))
    }

    override suspend fun update(todo: ToDo) {
        toDoDao.updateToDo(ToDoMapper.toEntity(todo))
    }

    override suspend fun delete(todo: ToDo) {
        toDoDao.deleteToDo(todo.id)
    }

    override suspend fun reorder(orderedIds: List<Int>) {
        toDoDao.updatePositions(orderedIds)
    }

}
