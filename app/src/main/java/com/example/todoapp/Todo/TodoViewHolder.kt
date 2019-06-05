package com.example.todoapp.Todo

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.todoapp.R
import com.example.todoapp.data.local.models.Todo

class TodoViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val title: TextView = itemView.findViewById(R.id.title)
    private val firstLetter: TextView = itemView.findViewById(R.id.first_letter)
    private val priorityImageView: ImageView = itemView.findViewById(R.id.priority_imgView)

    fun configureWith(todo: Todo) {
        title.text = todo.title
        firstLetter.text = todo.title.first().toUpperCase().toString()
        priorityImageView.setImageResource(getImage(todo.priority))
    }

    private fun getImage(priority: Int): Int {
        when (priority) {
            1 -> return R.drawable.low_priority
            2 -> return R.drawable.medium_priority
            else -> return R.drawable.high_priority
        }
    }
}