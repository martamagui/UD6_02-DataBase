package com.marta.ud6_01_networkud6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.marta.ud6_01_networkud6.databinding.ActivityMainBinding
import com.marta.ud6_01_networkud6.model.Task
import com.marta.ud6_01_networkud6.model.TaskList
import com.marta.ud6_01_networkud6.model.toTaskFromTaskEntity
import com.marta.ud6_01_networkud6.provider.api.TaskApi
import com.marta.ud6_01_networkud6.provider.db.DataBaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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