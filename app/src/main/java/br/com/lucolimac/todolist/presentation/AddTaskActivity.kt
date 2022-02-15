package br.com.lucolimac.todolist.presentation

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.lucolimac.todolist.databinding.ActivityAddTaskBinding
import br.com.lucolimac.todolist.datasourse.TaskDataSource
import br.com.lucolimac.todolist.extensions.format
import br.com.lucolimac.todolist.extensions.text
import br.com.lucolimac.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import java.sql.Time
import java.time.LocalTime
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListners()
        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.find(taskId)?.let {
                binding.etTitle.text = it.title
                binding.etDescription.text = it.description
                binding.etDate.text = it.date
                binding.etHour.text = it.time
            }
        }
    }

    private fun setupListners() {
        binding.etDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                timeZone.getOffset(Date().time) * -1
                binding.etDate.text = Date(it + timeZone.getOffset(Date().time) * -1).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }
        binding.etHour.editText?.setOnClickListener {
            val timePicker =
                MaterialTimePicker.Builder().setTimeFormat(CLOCK_24H).build()
            timePicker.addOnPositiveButtonClickListener {
                val hourChoised =
                    if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                val minuteChoised =
                    if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                binding.etHour.text = "$hourChoised:$minuteChoised"
            }
            timePicker.show(supportFragmentManager, "TIME_PICKER_TAG")
        }
        binding.btCreateTask.setOnClickListener {
            TaskDataSource.insertTask(getTask())
            setResult(CREATE_TASK_CODE)
            finish()
        }
        binding.btCancel.setOnClickListener { finish() }
    }

    private fun getTask(): Task {
        return Task(
            binding.etTitle.text,
            binding.etDescription.text,
            binding.etDate.text,
            binding.etHour.text,
            intent.getIntExtra(TASK_ID, 0)
        )
    }

    companion object {
        const val CREATE_TASK_CODE = 1000
        const val TASK_ID = "task_id"
    }
}