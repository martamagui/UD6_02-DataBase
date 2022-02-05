package com.marta.ud6_01_networkud6.usescases.tasks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.marta.ud6_01_networkud6.R
import com.marta.ud6_01_networkud6.databinding.FragmentDetailTaskBinding
import com.marta.ud6_01_networkud6.model.Task
import com.marta.ud6_01_networkud6.provider.api.TaskApi
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response


class DetailTaskFragment : Fragment() {
    private var _binding: FragmentDetailTaskBinding? = null
    private val binding
        get() = _binding!!
    private val args: DetailTaskFragmentArgs by navArgs()
    private lateinit var task: Task
    private var editMode = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailTaskBinding.inflate(inflater, container, false)
        return binding.root
    }
    //TODO add BD functionalities here
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val taskId= args.taskId
        requestTaskById(taskId)
        changeToViewDetailMode()
        binding.btnEdit.setOnClickListener {
            if (editMode == true) {
                changeToViewDetailMode()
                editMode = false
            } else {
                changeToEditMode()
                editMode = true
            }
        }
        binding.btnDelete.setOnClickListener {
            deleteTaskById(taskId)
        }
        binding.btnSave.setOnClickListener{
            val description = binding.etDetailTaskDescription.text.toString()
            val title = binding.etDetailTaskTitle.text.toString()
            val editedTask = Task(taskId,task.listIdFk,description,"Pendiente",title)
            editTask(editedTask)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Change to edit mode
    private fun changeToEditMode() {
        with(binding) {
            etDetailTaskTitleContainer.visibility = View.VISIBLE
            etDetailTaskDescriptionContainer.visibility = View.VISIBLE
            btnSave.visibility = View.VISIBLE
            tvDetailTaskTitle.visibility = View.INVISIBLE
            tvDetailDescription.visibility = View.INVISIBLE
            tvStatus.visibility = View.INVISIBLE
        }
    }

    private fun changeToViewDetailMode() {
        with(binding) {
            etDetailTaskTitleContainer.visibility = View.INVISIBLE
            etDetailTaskDescriptionContainer.visibility = View.INVISIBLE
            btnSave.visibility = View.INVISIBLE
            tvDetailTaskTitle.visibility = View.VISIBLE
            tvDetailDescription.visibility = View.VISIBLE
            tvStatus.visibility = View.VISIBLE
        }
    }

    private fun setTexts(task: Task) {
        with(binding) {
            tvDetailTaskTitle.text = task.title
            tvDetailDescription.text = task.description
            tvStatus.text = task.state
            etDetailTaskDescription.setText(task.description)
            etDetailTaskTitle.setText(task.title)
        }
    }

    private fun viewTotaskWasDeleted() {
        changeToViewDetailMode()
        with(binding) {
            tvDetailTaskTitle.text = getString(R.string.deleted)
            tvDetailDescription.text = getString(R.string.deleted)
            tvStatus.text = getString(R.string.deleted)
            btnEdit.isEnabled = false
            btnSave.isEnabled = false
            btnDelete.isEnabled = false
        }
    }

    //Request
    private fun requestTaskById(taskId: Int) {
        val service = TaskApi.service.getTaskById(taskId)
        val call = service.enqueue(object : Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if (response.isSuccessful) {
                    task = response.body()!!
                    setTexts(task)
                } else {
                    Toast.makeText(context, "(╯°□°）╯︵ ┻━┻ Format faliure", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                Toast.makeText(
                    context,
                    "FALIURE(╯°□°）╯︵ ┻━┻ Connection faliure ",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("faliure", "$t")
            }
        })
    }

    private fun deleteTaskById(taskId: Int) {
        val service = TaskApi.service.deleteTask(taskId)
        val call = service.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    viewTotaskWasDeleted()
                } else {
                    Toast.makeText(context, R.string.deleted_error, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(context, R.string.connection_error, Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

    private fun editTask(editedTask: Task){

        val service = TaskApi.service.editTask(editedTask.taskId,editedTask.title,editedTask.description)
        val call = service.enqueue(object : Callback<Task>{
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if(response.isSuccessful){
                    task = response.body()!!
                    setTexts(response.body()!!)
                    changeToViewDetailMode()
                }else{
                    Toast.makeText(context, R.string.format_error, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            override fun onFailure(call: Call<Task>, t: Throwable) {
                Toast.makeText(context, R.string.connection_error, Toast.LENGTH_SHORT)
                .show()
            }
        })
    }
}