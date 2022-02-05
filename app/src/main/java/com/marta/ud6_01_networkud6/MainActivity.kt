package com.marta.ud6_01_networkud6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.marta.ud6_01_networkud6.databinding.ActivityMainBinding
import com.marta.ud6_01_networkud6.model.Task
import com.marta.ud6_01_networkud6.model.TaskList
import com.marta.ud6_01_networkud6.model.toListOfEntityList
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
        requestTaskList()
        setContentView(binding.root)
    }

    //Request
    private fun requestTaskList() {
        val service = TaskApi.service.getTaskLists()
        val call = service.enqueue(object : Callback<List<TaskList>> {
            override fun onResponse(
                call: Call<List<TaskList>>,
                response: Response<List<TaskList>>
            ) {
                if (response.isSuccessful) {
                    if (lists.size > 0) {
                        lists.clear()
                    }
                    response.body()?.let { lists.addAll(it) }
                    addAllListsToDB(lists)
                    for (item in lists) {
                        requestTask(item.listId)
                    }
                }
            }

            override fun onFailure(call: Call<List<TaskList>>, t: Throwable) {
                connectionFaliure(t)
            }
        })
    }

    private fun requestTask(listId: Int) {
        val service = TaskApi.service.getTaskByListId(listId)
        val call = service.enqueue(object : Callback<List<Task>> {
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if (response.isSuccessful) {
                    if (tasks.size > 0) {
                        tasks.clear()
                    }
                    response.body()?.let { tasks.addAll(it) }
                    addAllTasksToDB(tasks)

//                    adapter.submitList(taskList)
//                    adapter.notifyDataSetChanged()
//                    showHideMessage()
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                connectionFaliure(t)
            }
        })
    }

    //DB
    private fun addAllListsToDB(lista: MutableList<TaskList>) {
        lifecycleScope.launch(Dispatchers.IO) {
            DataBaseRepository.getInstance(applicationContext).databaseDao()
                .addAllLists(lista.toListOfEntityList())
        }
    }

    private fun addAllTasksToDB(tasks: MutableList<Task>) {
        lifecycleScope.launch(Dispatchers.IO) {
            DataBaseRepository.getInstance(applicationContext).databaseDao()
                .addAllTask(tasks.toTaskFromTaskEntity())
        }
    }

    private fun connectionFaliure(t: Throwable) {
        Toast.makeText(
            applicationContext,
            "Conexión no disponible",
            Toast.LENGTH_SHORT
        ).show()
        Log.e("faliure", "$t")
    }

}