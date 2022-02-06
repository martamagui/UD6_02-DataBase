package com.marta.ud6_01_networkud6.usescases.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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


class TaskListFragment : Fragment() {
    private var _binding: FragmentTaskListBinding? = null
    private val binding
        get() = _binding!!
    private val lista: MutableList<TaskListEntity> = mutableListOf()
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
            getAllMyLists()
            updateRV()
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

    private fun showHideMessage() {
        if (lista.size > 0) {
            binding.tvNoList.visibility = View.INVISIBLE
        } else {
            binding.tvNoList.visibility = View.VISIBLE
        }
    }

    private fun clearText() {
        binding.tfNewList.setText("")
    }

    private fun updateRV() {
        adapter.submitList(lista)
        showHideMessage()
    }

    private fun checkField(): Boolean {
        if (binding.tfNewList.text.toString().count() <= 0) {
            return false
        }
        return true
    }

    //Utils
    private fun getAnId(): Int {
        var id: Int = 1
        if (lista.size > 0) {
            id = lista.get(0).listId + 1
        }
        return id
    }

    private fun addList(title: String) {
        val newList = TaskListEntity(getAnId(), title, 1)
        lista.add(newList)
        lista.sortByDescending { it.listId }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            addListToDB(newList)
            updateRV()
            showHideMessage()
        }
        clearText()
        Toast.makeText(context, "Guardado", Toast.LENGTH_SHORT).show()
    }


    //DataBase
    private suspend fun addListToDB(lista: TaskListEntity) {
        DataBaseRepository.getInstance(requireContext()).databaseDao()
            .addList(lista)
    }

    private fun deleteList(item: TaskListEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            val list = item
            DataBaseRepository.getInstance(requireContext()).databaseDao()
                .deleteListAndItsTasks(list.listId)
            lista.remove(item)
        }
    }

    private suspend fun getAllMyLists() {
        lista.clear()
        lista.addAll(
            DataBaseRepository.getInstance(requireContext()).databaseDao().findUserLists(userId)
        )
        lista.sortByDescending { it.listId }
    }

    //Navigation
    private fun viewChange(list: TaskListEntity) {
        val action =
            TaskListFragmentDirections.actionTaskListFragmentToTasksFragment(list.listId, list.name)
        findNavController().navigate(action)
    }
}