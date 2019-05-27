package com.example.todoapp.Todo

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.todoapp.R
import com.example.todoapp.data.local.models.Todo

class TodoViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val title: TextView = itemView.findViewById(R.id.title)
    val first_letter: TextView = itemView.findViewById(R.id.first_letter)
    val priority_imgView: ImageView = itemView.findViewById(R.id.priority_imgView)

    fun configureWith(todo: Todo) {
        title.text = todo.title
        first_letter.text = todo.title.first().toUpperCase().toString()
        priority_imgView.setImageResource(getImage(todo.priority))
    }

    private fun getImage(priority: Int): Int {
        if (priority == 1) {
            return R.drawable.low_priority
        } else if (priority == 2) {
            return R.drawable.medium_priority
        } else {
            return R.drawable.high_priority
        }
    }
}