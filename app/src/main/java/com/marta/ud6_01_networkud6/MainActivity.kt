package com.marta.ud6_01_networkud6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.marta.ud6_01_networkud6.databinding.ActivityMainBinding
import com.marta.ud6_01_networkud6.model.Task
import com.marta.ud6_01_networkud6.model.TaskList


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val lists: MutableList<TaskList> = mutableListOf()
    private var tasks: MutableList<Task> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}