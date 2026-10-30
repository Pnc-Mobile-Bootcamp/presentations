package com.pnc.jetpackcomposedemos.features.todo.domain

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateToDoUseCase @Inject constructor(
    private val toDoRepository: ToDoRepository
) {

    suspend operator fun invoke(todo: ToDo) {
        toDoRepository.insert(todo)
    }

}

