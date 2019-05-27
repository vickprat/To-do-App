package com.example.todoapp.Todo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.todoapp.R
import com.example.todoapp.data.local.models.Todo


class TodoAdapter : RecyclerView.Adapter<TodoViewHolder>() {

    private var onTodoItemPressedListener: OnTodoItemPressedListener? = null

    var todoList: List<Todo> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val layout = if (itemCount == 0) R.layout.empty_view else R.layout.todo_item_view
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onTodoItemPressedListener!!.onTodoItemPressed(todoList.get(position))
        }
        holder.itemView.setOnLongClickListener {
            onTodoItemPressedListener!!.onTodoItemLongPressed(todoList.get(position))
            true
        }
        holder.configureWith(todoList.get(position))
    }

    fun setTodoItemPressedListener(onTodoItemPressedListener: OnTodoItemPressedListener) {
        this.onTodoItemPressedListener = onTodoItemPressedListener
    }

    interface OnTodoItemPressedListener {
        fun onTodoItemPressed(todo: Todo)
        fun onTodoItemLongPressed(todo: Todo)
    }
}