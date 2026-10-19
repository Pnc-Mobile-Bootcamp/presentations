package com.pnc.jetpackcomposedemos.features.todo.domain

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateToDoUseCase @Inject constructor(
    private val toDoRepository: ToDoRepository
) {

    suspend operator fun invoke(toDo: ToDo) {
        toDoRepository.update(toDo)
    }

}


