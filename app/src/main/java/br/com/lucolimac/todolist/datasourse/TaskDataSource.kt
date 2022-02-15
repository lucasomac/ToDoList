package br.com.lucolimac.todolist.datasourse

import br.com.lucolimac.todolist.model.Task

object TaskDataSource {
    private val listTask = arrayListOf<Task>()
    fun getList() = listTask
    fun insertTask(task: Task) {
        when (task.id) {
            0 -> listTask.add(task.copy(id = listTask.size + 1))
            else -> {
                listTask.remove(task)
                listTask.add(task)
            }
        }
    }

    fun find(taskId: Int) = listTask.find { it.id == taskId }
    fun delete(task: Task) {
        listTask.remove(task)
    }

}