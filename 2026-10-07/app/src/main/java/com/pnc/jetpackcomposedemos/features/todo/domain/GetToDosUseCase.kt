package com.pnc.jetpackcomposedemos.features.todo.domain

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetToDosUseCase @Inject constructor(
    private val toDoRepository: ToDoRepository
) {

    suspend operator fun invoke(): List<ToDo> {
        return toDoRepository.getToDos()
    }

}


