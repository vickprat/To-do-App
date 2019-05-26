package com.example.todoapp.data.local

import android.arch.persistence.room.*
import com.example.todoapp.data.local.models.Todo

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo ORDER BY todoId ASC")
    fun getToDoList(): List<Todo>

    @Query("SELECT * FROM todo WHERE todoId=:todoId")
    fun getToDoItem(todoId: Int): Todo

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveToDo(todo: Todo)

    @Update
    fun updateToDo(todo: Todo)

    @Delete
    fun deleteToDo(todo: Todo)
}