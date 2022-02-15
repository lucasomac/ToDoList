package br.com.lucolimac.todolist.presentation

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import br.com.lucolimac.todolist.adapter.TaskAdapter
import br.com.lucolimac.todolist.databinding.ActivityMainBinding
import br.com.lucolimac.todolist.datasourse.TaskDataSource

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvTasks.adapter = adapter
        updateList()
        setupListeners()
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == CREATE_TASK_CODE) {
                // There are no request codes
                val data: Intent? = it.data
                binding.rvTasks.adapter = adapter
                updateList()
            }
        }

    private fun updateList() {
        val list = TaskDataSource.getList()
        when (list.isEmpty()) {
            true -> {
                binding.includeEmptyState.root.visibility = View.VISIBLE
                binding.rvTasks.visibility = View.GONE
            }
            false -> {
                adapter.submitList(TaskDataSource.getList())
                binding.includeEmptyState.root.visibility = View.GONE
            }
        }
    }

    private fun setupListeners() {
        binding.fabAddTask.setOnClickListener {
            resultLauncher.launch(Intent(this, AddTaskActivity::class.java))
        }
        adapter.listenerEdit = {
            resultLauncher.launch(
                Intent(
                    this,
                    AddTaskActivity::class.java
                ).apply { putExtra(TASK_ID, it.id) })
        }
        adapter.listenerDelete = {
            TaskDataSource.delete(it)
            updateList()
        }
    }

    companion object {
        const val CREATE_TASK_CODE = 1000
        const val TASK_ID = "task_id"
    }
}