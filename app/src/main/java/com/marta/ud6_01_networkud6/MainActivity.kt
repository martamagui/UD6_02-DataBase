package com.marta.ud6_01_networkud6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.marta.ud6_01_networkud6.databinding.ActivityMainBinding
import com.marta.ud6_01_networkud6.model.TaskList
import com.marta.ud6_01_networkud6.model.toListOfEntityList
import com.marta.ud6_01_networkud6.provider.api.TaskApi
import com.marta.ud6_01_networkud6.provider.db.DataBaseRepository
import com.marta.ud6_01_networkud6.provider.db.entitties.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val lista: MutableList<TaskList> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestTaskList()
        setContentView(binding.root)
    }
    private fun addAllListsToDB(lista: MutableList<TaskList>) {
        lifecycleScope.launch(Dispatchers.IO) {
            DataBaseRepository.getInstance(applicationContext).databaseDao()
                .addAllLists(lista.toListOfEntityList())
        }
    }
    private fun requestTaskList() {
        val service = TaskApi.service.getTaskLists()
        val call = service.enqueue(object : Callback<List<TaskList>> {
            override fun onResponse(
                call: Call<List<TaskList>>,
                response: Response<List<TaskList>>
            ) {
                if (response.isSuccessful) {
                    if (lista.size > 0) {
                        lista.clear()
                    }
                    response.body()?.let { lista.addAll(it) }
                    addAllListsToDB(lista)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "RESPONSE(╯°□°）╯︵ ┻━┻ Connection faliure",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<List<TaskList>>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "FALIURE(╯°□°）╯︵ ┻━┻ Connection faliure ",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("faliure", "$t")
            }
        })
    }

}