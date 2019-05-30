package com.example.todoapp.Todo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.RadioGroup
import com.example.todoapp.R
import com.example.todoapp.data.local.TodoDatabase
import com.example.todoapp.data.local.models.Todo
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
                todoDatabase.getToDoDao().saveToDo(todo)
                finish()
            }
        } else {
            add_todo.text = getString(R.string.update)
            val todoId = intent.getIntExtra("todoId", 0)
            title_ed.setText(title)
            detail_ed.setText(detail)
            add_todo.setOnClickListener{
                val todo = Todo(title_ed.text.toString(), priority, todoId)
                todo.detail = detail_ed.text.toString()
                todoDatabase.getToDoDao().updateToDo(todo)
                finish()
            }
        }
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