package com.pnc.jetpackcomposedemos.features.todo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo ORDER BY position, id")
    suspend fun getToDos(): List<ToDoEntity>

    @Query("SELECT * FROM todo WHERE id = :id")
    suspend fun getToDo(id: Int): ToDoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDo(toDo: ToDoEntity)

    @Update
    suspend fun updateToDo(toDo: ToDoEntity)

    @Query("DELETE FROM todo WHERE id = :id")
    suspend fun deleteToDo(id: Int)

    @Query("SELECT COALESCE(MAX(position), 0) from todo")
    suspend fun maxPosition(): Int

    @Query("UPDATE todo SET position = :position WHERE id = :id")
    suspend fun setPosition(id: Int, position: Int)

    @Transaction
    suspend fun updatePositions(orderedIds: List<Int>) {
        orderedIds.forEachIndexed { index, id ->
            setPosition(id, index + 1)
        }
    }

}





