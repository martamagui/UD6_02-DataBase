package com.marta.ud6_01_networkud6.usescases.tasks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.marta.ud6_01_networkud6.databinding.FragmentAddTaskBinding
import com.marta.ud6_01_networkud6.model.Task
import com.marta.ud6_01_networkud6.provider.api.TaskApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddTaskFragment : Fragment() {
    private var _binding: FragmentAddTaskBinding? = null
    private val binding
        get() = _binding!!
    private val args: AddTaskFragmentArgs by navArgs()
    private var newId = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMyId()
        binding.btnSave.setOnClickListener {
            if(checkTV()){
                val title: String = binding.etTitleTask.text.toString()
                val description : String = binding.etDescriptionTask.text.toString()
                val task = Task(newId, args.listId,description,"Pendiente",title)
                addTask(task)
                getMyId()
            }
        }
    }
    //Check
    private fun checkTV():Boolean{
        if(binding.etTitleTask.text.toString().count()>0){
            return true
        }
        return false;
    }
    private fun clearTxtFiles(){
        with(binding){
            etTitleTask.setText("")
            etDescriptionTask.setText("")
        }
    }

    //Api
    fun addTask(task: Task) {

        val service = TaskApi.service.addTask(task)
        val call = service.enqueue(object : Callback<Boolean>{
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Guardado", Toast.LENGTH_SHORT)
                        .show()
                    clearTxtFiles()
                } else {
                    Toast.makeText(context, "(╯°□°）╯︵ ┻━┻ Format faliure", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Toast.makeText(context, "(╯°□°）╯︵ ┻━┻ Error de conexión", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    fun getMyId() {
        val service = TaskApi.service.getMyId()
        val call = service.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    newId = response.body()!!
                    Log.e("size", response.body()!!.toString())
                } else {
                    Toast.makeText(context, "(╯°□°）╯︵ ┻━┻ Format faliure", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.e("faliure", "$t")
            }
        })
    }

}