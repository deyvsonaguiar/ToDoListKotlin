package com.deyvsonaguiar.todolist.datasource

import com.deyvsonaguiar.todolist.model.Task

object TaskDataSource {
    private var list = arrayListOf<Task>()

    fun getList() = list

    fun insertTask(task: Task) {
        list.add(task.copy(id = list.size + 1))
    }
}