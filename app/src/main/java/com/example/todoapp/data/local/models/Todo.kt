package com.example.todoapp.data.local.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "todo")
data class Todo(@PrimaryKey(autoGenerate = true) var todoId: Int = 0,
                @ColumnInfo(name = "todo_title") var title: String = "",
                @ColumnInfo(name = "todo_priority") var priority: Int = 0) {
    var detail: String = ""
}