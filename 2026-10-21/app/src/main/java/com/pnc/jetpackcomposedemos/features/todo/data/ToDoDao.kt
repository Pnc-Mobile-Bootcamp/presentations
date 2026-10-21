package com.pnc.jetpackcomposedemos.features.todo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo")
    suspend fun getToDos(): List<ToDoEntity>

    @Query("SELECT * FROM todo WHERE id = :id")
    suspend fun getToDo(id: Int): ToDoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDo(toDo: ToDoEntity)

    @Update
    suspend fun updateToDo(toDo: ToDoEntity)

}