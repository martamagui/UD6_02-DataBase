package com.marta.ud6_01_networkud6.usescases.tasks;

import android.os.Bundle
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
import com.marta.ud6_01_networkud6.provider.db.entitties.ListWithTasks
import com.marta.ud6_01_networkud6.provider.db.entitties.TaskEntity
import com.marta.ud6_01_networkud6.usescases.common.TaskAdapter
import kotlinx.coroutines.*


class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding? = null
    private val binding
        get() = _binding!!
    private val args: TasksFragmentArgs by navArgs()
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
        setUI()
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val listWithTasks = getListWithTasksFromDB()
            getTasks(listWithTasks)
        }
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

    private fun showHideMessage(listWithTasks: List<TaskEntity>?) =
        if (!listWithTasks.isNullOrEmpty()) {
            binding.tvNoTask.visibility = View.INVISIBLE
        } else {
            binding.tvNoTask.visibility = View.VISIBLE
        }

    private fun updateRV(listWithTasks: ListWithTasks) {
        if (listWithTasks.tasks?.size ?: -1 > 0) {
            adapter.submitList(listWithTasks.tasks!!)
        }
        showHideMessage(listWithTasks.tasks)
    }

    private fun deletedListview() {
        binding.tvListTitle.text = "Eliminado"
        binding.fabAddTask.isEnabled = false
        binding.ivBin.visibility = View.GONE

    }

    //Navigation
    private fun toDetailView(taskId: Int) {
        val action = TasksFragmentDirections.actionTasksFragmentToDetailTaskFragment(taskId)
        findNavController().navigate(action)
    }

    private fun viewChangeAddTaskView(listId: Int) {
        val action = TasksFragmentDirections.actionTasksFragmentToAddTaskFragment(listId)
        findNavController().navigate(action)
    }


    //DB
    private suspend fun getListWithTasksFromDB(): ListWithTasks {
        val listAndTasks = DataBaseRepository.getInstance(requireContext()).databaseDao()
            .findTaskFromList(listId)
        return listAndTasks
    }

    private fun deleteList(id: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            DataBaseRepository.getInstance(requireContext()).databaseDao().deleteTaskListById(id)
            withContext(Dispatchers.Main) {
                deletedListview()
            }
        }
    }

    //Utils

    private fun getTasks(listWithTasks: ListWithTasks) {
        if (listWithTasks?.tasks?.count()!! > 0) {
            updateRV(listWithTasks)
        }
    }
}