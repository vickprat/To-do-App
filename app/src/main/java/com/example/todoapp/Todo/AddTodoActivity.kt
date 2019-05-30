package com.example.todoapp.Todo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.RadioGroup
import com.example.todoapp.R
import com.example.todoapp.data.local.TodoDatabase
import com.example.todoapp.data.local.models.Todo
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_todo.*

class AddTodoActivity: AppCompatActivity(), RadioGroup.OnCheckedChangeListener {

    lateinit var todoDatabase: TodoDatabase
    private var priority = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_todo)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        todoDatabase = TodoDatabase.getInstance(this)
        radioGroup.setOnCheckedChangeListener(this)

        val title = intent.getStringExtra("title")
        val detail = intent.getStringExtra("detail")
        if (title == null || title == "") {
            add_todo.setOnClickListener{
                val todo = Todo(title_ed.text.toString(), priority)
                todo.detail = detail_ed.text.toString()
                saveToDoInDatabase(todo).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ finish() }){
                        print("Saving to do failed")
                    }
            }
        } else {
            add_todo.text = getString(R.string.update)
            val todoId = intent.getIntExtra("todoId", 0)
            title_ed.setText(title)
            detail_ed.setText(detail)
            add_todo.setOnClickListener{
                val todo = Todo(title_ed.text.toString(), priority, todoId)
                todo.detail = detail_ed.text.toString()
                updateToDoInDatabase(todo).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ finish() }){
                        print("Updating to do failed")
                    }
            }
        }
    }

    fun saveToDoInDatabase(todo: Todo): Maybe<Unit> {
        return Maybe.fromCallable {
            todoDatabase.getToDoDao().saveToDo(todo)
        }.subscribeOn(Schedulers.io())
    }

    fun updateToDoInDatabase(todo: Todo): Maybe<Unit> {
        return Maybe.fromCallable {
            todoDatabase.getToDoDao().updateToDo(todo)
        }.subscribeOn(Schedulers.io())
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        if (checkedId == R.id.medium) {
            priority = 2
        } else if (checkedId == R.id.high) {
            priority = 3
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            startActivity(Intent(this, TodoActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}