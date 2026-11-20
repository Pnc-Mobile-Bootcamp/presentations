package com.pnc.jetpackcomposedemos.features.todo.domain

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReorderToDosUseCase @Inject constructor(
    private val toDoRepository: ToDoRepository
) {

    // [orderedIds] lists every to-do id in the order the user wants
    suspend operator fun invoke(orderedIds: List<Int>) {
        toDoRepository.reorder(orderedIds)
    }

}


