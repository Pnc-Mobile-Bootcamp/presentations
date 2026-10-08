package com.pnc.jetpackcomposedemos.features.todo.data

import com.pnc.jetpackcomposedemos.features.todo.domain.ToDo

object ToDoMapper {

    fun toDomain(entity: ToDoEntity): ToDo {
        return ToDo(
            id = entity.id,
            title = entity.title,
            completed = entity.completed
        )
    }

    fun toEntity(domain: ToDo): ToDoEntity {
        return ToDoEntity(
            id = domain.id,
            title = domain.title,
            completed = domain.completed
        )
    }

}