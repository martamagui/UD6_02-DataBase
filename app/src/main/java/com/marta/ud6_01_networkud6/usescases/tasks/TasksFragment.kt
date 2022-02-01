package com.marta.ud6_01_networkud6.usescases.tasks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.marta.ud6_01_networkud6.databinding.FragmentTasksBinding
import com.marta.ud6_01_networkud6.model.Task
import com.marta.ud6_01_networkud6.provider.api.TaskApi
import com.marta.ud6_01_networkud6.usescases.common.TaskAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding? = null
    private val binding
        get() = _binding!!
    private val args: TasksFragmentArgs by navArgs()
    private var taskList: MutableList<Task> = mutableListOf()
    private var listId: Int = 0
    private val adapter = TaskAdapter {
        toDetailView(it.taskId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvTasks.adapter = adapter
        binding.rvTasks.layoutManager = LinearLayoutManager(context)
        binding.tvListTitle.text = args.listName
        listId = args.listIdFk
        requestTask(listId)
        binding.fabAddTask.setOnClickListener {
            viewChangeAddTaskView(args.listIdFk)
        }
        binding.ivBin.setOnClickListener {
            deleteList(listId)
            requestTask(listId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Elements visibility
    private fun showHideMessage() {
        Log.d("list size", taskList.size.toString())
        if (taskList.size > 0) {
            binding.tvNoTask.visibility = View.INVISIBLE
        } else {
            binding.tvNoTask.visibility = View.VISIBLE
        }
    }

    //ViewChange
    private fun toDetailView(taskId: Int) {
        val action = TasksFragmentDirections.actionTasksFragmentToDetailTaskFragment(taskId)
        findNavController().navigate(action)
    }

    private fun viewChangeAddTaskView(listId: Int) {
        val action = TasksFragmentDirections.actionTasksFragmentToAddTaskFragment(listId)
        findNavController().navigate(action)
    }

    //Request
    private fun requestTask(listId: Int) {
        val service = TaskApi.service.getTaskByListId(listId)
        val call = service.enqueue(object : Callback<List<Task>> {
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if (response.isSuccessful) {
                    if (taskList.size > 0) {
                        taskList.clear()
                    }
                    response.body()?.let { taskList.addAll(it) }
                    adapter.submitList(taskList)
                    adapter.notifyDataSetChanged()
                    showHideMessage()
                } else {
                    Toast.makeText(context, "(╯°□°）╯︵ ┻━┻ Format faliure", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                Toast.makeText(
                    context,
                    "FALIURE(╯°□°）╯︵ ┻━┻ Connection faliure ",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("faliure", "$t")
            }
        })
    }


    private fun deleteList(listId: Int) {
        val service = TaskApi.service.deleteList(listId)
        val call = service.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (!response.isSuccessful) {
                    Log.d("Item", "(╯°□°）╯︵ ┻━┻ Formato incorrecto")
                } else {
                    binding.rvTasks.visibility = View.GONE
                    binding.ivBin.visibility = View.GONE
                    binding.tvListTitle.text = "Lista eliminada"
                    binding.tvNoTask.text = "Esta lista fue eliminada"
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.d("Item", "(╯°□°）╯︵ ┻━┻ Formato incorrecto $t")
            }
        })
    }

}