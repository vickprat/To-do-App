package com.example.todoapp.Todo

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.data.local.TodoDatabase
import com.example.todoapp.data.local.models.Todo
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class TodoActivity: AppCompatActivity(), TodoAdapter.OnTodoItemPressedListener {
    lateinit var todoDatabase: TodoDatabase
    lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todoDatabase = TodoDatabase.getInstance(this)

        todoAdapter = TodoAdapter()
        todoAdapter.setTodoItemPressedListener(this)

        add_todo.setOnClickListener{
            startActivity(Intent(this, AddTodoActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        getToDoListFromDatabase()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                todoAdapter.todoList = result
                todoList.adapter = todoAdapter
                todoList.layoutManager = LinearLayoutManager(this)
                todoList.hasFixedSize()
            }
    }

    fun getToDoListFromDatabase(): Single<List<Todo>> {
        return Single.fromCallable {
            todoDatabase.getToDoDao().getToDoList()
        }.subscribeOn(Schedulers.io())
    }

    override fun onTodoItemPressed(todo: Todo) {
        val intent = Intent(this, AddTodoActivity::class.java)
        intent.putExtra("todoId", todo.todoId)
        intent.putExtra("title", todo.title)
        intent.putExtra("detail", todo.detail)
        intent.putExtra("priority", todo.priority)
        startActivity(intent)
    }

    override fun onTodoItemLongPressed(todo: Todo) {
        val alertDialog = AlertDialog.Builder(this)
            .setItems(R.array.dialog_list, DialogInterface.OnClickListener { dialog, which ->
                if (which == 0) {
                    val intent = Intent(this@TodoActivity, AddTodoActivity::class.java)
                    intent.putExtra("todoId", todo.todoId)
                    intent.putExtra("title", todo.title)
                    intent.putExtra("detail", todo.detail)
                    intent.putExtra("priority", todo.priority)
                    startActivity(intent)
                } else {
                    Single.fromCallable {
                        todoDatabase.getToDoDao().deleteToDo(todo)
                    }.subscribeOn(Schedulers.io())
                        .flatMap {
                        getToDoListFromDatabase()
                    }.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { result ->
                                todoAdapter.todoList = result
                            }
                        ) {
                            print("Deleting/updating to do failed")
                        }
                }
                dialog.dismiss()
            })
            .create()
        alertDialog.show()
    }
}