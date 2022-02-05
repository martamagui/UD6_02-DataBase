package com.marta.ud6_01_networkud6.usescases.tasks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.marta.ud6_01_networkud6.databinding.FragmentTasksBinding
import com.marta.ud6_01_networkud6.provider.db.DataBaseRepository
import com.marta.ud6_01_networkud6.provider.db.entitties.TaskEntity
import com.marta.ud6_01_networkud6.usescases.common.TaskAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding? = null
    private val binding
        get() = _binding!!
    private val args: TasksFragmentArgs by navArgs()
    private var tasks: MutableList<TaskEntity> = mutableListOf()
    private var listId: Int = 0
    private val adapter = TaskAdapter {
        toDetailView(it.taskId!!)
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
        getTasksFromDB()
        setUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //UI
    private fun setUI() {
        setAdapter()
        binding.tvListTitle.text = args.listName
        listId = args.listIdFk
        binding.fabAddTask.setOnClickListener {
            viewChangeAddTaskView(args.listIdFk)
        }
        binding.ivBin.setOnClickListener {
            deleteList(listId)
        }
    }

    private fun setAdapter() {
        binding.rvTasks.adapter = adapter
        binding.rvTasks.layoutManager = LinearLayoutManager(context)
    }

    private fun showHideMessage() {
        Log.d("list size", tasks.size.toString())
        if (tasks.size > 0) {
            binding.tvNoTask.visibility = View.INVISIBLE
        } else {
            binding.tvNoTask.visibility = View.VISIBLE
        }
    }

    private fun updateRV() {
        adapter.submitList(tasks)
        showHideMessage()
        adapter.notifyDataSetChanged()
    }

    private fun deletedListview() {
        binding.tvListTitle.text = "Eliminado"
        binding.fabAddTask.isEnabled = false
        binding.ivBin.visibility = View.GONE

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


    //DB
    private fun getTasksFromDB() {
        lifecycleScope.launch(Dispatchers.IO) {
            tasks.clear()
            tasks.addAll(
                DataBaseRepository.getInstance(requireContext()).databaseDao()
                    .findTaskFromList(listId)
            )
            withContext(Dispatchers.Main) {
                updateRV()
            }
        }
    }

    private fun deleteList(id: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            DataBaseRepository.getInstance(requireContext()).databaseDao().deleteTaskListById(id)
            withContext(Dispatchers.Main) {
                deletedListview()
            }
        }
    }


}