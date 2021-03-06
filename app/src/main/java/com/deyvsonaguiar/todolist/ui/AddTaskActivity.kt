package com.deyvsonaguiar.todolist.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.deyvsonaguiar.todolist.R
import com.deyvsonaguiar.todolist.databinding.ActivityAddTaskBinding
import com.deyvsonaguiar.todolist.datasource.TaskDataSource
import com.deyvsonaguiar.todolist.extensions.format
import com.deyvsonaguiar.todolist.extensions.text
import com.deyvsonaguiar.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.tilTitle.text = it.title
                binding.tilDate.text = it.date
                binding.tilHour.text = it.hour

            }
        }

        insertListeners()
        initToolbar()
    }

    private fun insertListeners() {
        //Listener no input Data
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                //binding.tilDate.editText?.setText(Date(it).format()) // modo padrão
                binding.tilDate.text = Date(it).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        //Listener no input Hora
        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H) //formato 24 horas do timePicker
                .build()
            timePicker.addOnPositiveButtonClickListener {

                //tratamento da hora e minuto para mostrar 2 digitos
                val minute = if(timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if(timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.tilHour.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager, null)
        }

        //Listener no button Cancelar
        binding.btnCancel.setOnClickListener {
            finish()
        }

        //Listener no button Criar tarefa
        binding.btnNewTask.setOnClickListener {
            val task = Task(
                title = binding.tilTitle.text,
                date = binding.tilDate.text,
                hour =  binding.tilHour.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun initToolbar() {
        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}