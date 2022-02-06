package com.marta.ud6_01_networkud6.usescases.tasks

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.marta.ud6_01_networkud6.databinding.FragmentTaskListBinding
import com.marta.ud6_01_networkud6.provider.db.DataBaseRepository
import com.marta.ud6_01_networkud6.provider.db.entitties.TaskListEntity
import com.marta.ud6_01_networkud6.usescases.common.TaskListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Entity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class TaskListFragment : Fragment() {
    private var _binding: FragmentTaskListBinding? = null
    private val binding
        get() = _binding!!
    private val adapter = TaskListAdapter({ deleteList(it) }) {
        viewChange(it)
    }
    private val userId: Int = 1

    //TODO Add a loading animation
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val list : List<TaskListEntity> = getAllMyLists()
            updateRV(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // UI
    private fun setUI() {
        setAdapter()
        binding.fabAddList.setOnClickListener {
            if (checkField()) {
                val text = binding.tfNewList.text.toString()
                addList(text)
            } else {
                Toast.makeText(context, "Texto vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setAdapter() {
        binding.rvTaskList.adapter = adapter
        binding.rvTaskList.layoutManager = LinearLayoutManager(context)
    }

    private fun showHideMessage(list: List<TaskListEntity>) {
        if (list.size > 0) {
            binding.tvNoList.visibility = View.INVISIBLE
        } else {
            binding.tvNoList.visibility = View.VISIBLE
        }
    }

    private fun clearText() {
        binding.tfNewList.setText("")
    }

    private fun updateRV(list: List<TaskListEntity>) {
        adapter.submitList(list)
        showHideMessage(list)
    }

    private fun checkField(): Boolean {
        if (binding.tfNewList.text.toString().count() <= 0) {
            return false
        }
        return true
    }

    //Utils
    private fun getDateAndTime(): String{
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate : String = sdf.format(Date())
        return currentDate
    }

    private fun addList(title: String) {
        val newList = TaskListEntity(0, title, getDateAndTime(),5, userId)
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            addListToDB(newList)
            val list : List<TaskListEntity> = getAllMyLists()
            updateRV(list)
            showHideMessage(list)
        }
        clearText()
        Toast.makeText(context, "Guardado", Toast.LENGTH_SHORT).show()
    }


    //DataBase
    private suspend fun addListToDB(newList: TaskListEntity) {
        DataBaseRepository.getInstance(requireContext()).databaseDao()
            .addList(newList)
    }

    private fun deleteList(item: TaskListEntity) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            DataBaseRepository.getInstance(requireContext()).databaseDao()
                .deleteListAndItsTasks(item.listId)
            val list : List<TaskListEntity> = getAllMyLists()
            updateRV(list)
        }
    }

    private suspend fun getAllMyLists(): List<TaskListEntity> {
        val list: List<TaskListEntity> = DataBaseRepository.getInstance(requireContext()).databaseDao().findUserLists(userId)
        list.sortedByDescending { it.listId }
        return list
    }

    //Navigation
    private fun viewChange(list: TaskListEntity) {
        val action =
            TaskListFragmentDirections.actionTaskListFragmentToTasksFragment(list.listId, list.name)
        findNavController().navigate(action)
    }
}