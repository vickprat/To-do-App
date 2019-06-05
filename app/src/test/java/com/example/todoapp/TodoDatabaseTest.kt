package com.example.todoapp

import android.arch.persistence.room.Room
import android.content.Context
import com.example.todoapp.data.local.TodoDao
import com.example.todoapp.data.local.TodoDatabase
import com.example.todoapp.data.local.models.Todo
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class TodoDatabaseTest {
    private lateinit var todoDao: TodoDao
    private lateinit var database: TodoDatabase
    private var context: Context = mock(Context::class.java)

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(context, TodoDatabase::class.java).allowMainThreadQueries().build()
        todoDao = database.getToDoDao()
    }

    @After
    fun tearDown() = database.close()

    @Test
    fun should_insert_todo() {
        val todo = Todo(title = "ToDoTitle", priority = 1, todoId = 2)
        todoDao.saveToDo(todo)
        val todoTest = todoDao.getToDoItem(2)
        Assert.assertEquals(todo.title, todoTest.title)
        Assert.assertEquals(todo.priority, todoTest.priority)
    }

    @Test
    fun should_delete_todo() {
        val todo = Todo(title = "ToDoTitle", priority = 1, todoId = 3)
        val todo1 = Todo(title = "ToDoTitle", priority = 1, todoId = 4)
        todoDao.saveToDo(todo)
        todoDao.saveToDo(todo1)
        todoDao.deleteToDo(todo)
        Assert.assertEquals(todoDao.getToDoList().count(), 1)
    }
}
