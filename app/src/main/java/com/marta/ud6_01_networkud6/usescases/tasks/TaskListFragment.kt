package com.marta.ud6_01_networkud6.usescases.tasks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.marta.ud6_01_networkud6.databinding.FragmentTaskListBinding
import com.marta.ud6_01_networkud6.model.TaskList
import com.marta.ud6_01_networkud6.model.toEntityTaskList
import com.marta.ud6_01_networkud6.provider.api.TaskApi
import com.marta.ud6_01_networkud6.provider.db.DataBaseRepository
import com.marta.ud6_01_networkud6.provider.db.entitties.TaskListEntity
import com.marta.ud6_01_networkud6.provider.db.entitties.toListOfList
import com.marta.ud6_01_networkud6.usescases.common.TaskListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TaskListFragment : Fragment() {
    private var _binding: FragmentTaskListBinding? = null
    private val binding
        get() = _binding!!
    private val lista: MutableList<TaskListEntity> = mutableListOf()
    private val adapter = TaskListAdapter({ deleteList(it) }) {
        viewChange(it.listId, it.name)
    }
    private val userId: Int = 1

    //TODO Add a loading animation
    //TODO crear una función que compruebe los ids de las listas antes de guardarlas
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvTaskList.adapter = adapter
        binding.rvTaskList.layoutManager = LinearLayoutManager(context)
        binding.fabAddList.setOnClickListener {
            if (checkField()) {
                val text = binding.tfNewList.text.toString()
                addList(text)
            }else{
                Toast.makeText(context, "Texto vacío", Toast.LENGTH_SHORT).show()
            }
        }
        getAllMyLists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // UI Related
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
        adapter.notifyDataSetChanged()
        showHideMessage()
    }

    private fun checkField(): Boolean {
        if (binding.tfNewList.text.toString().count() <= 0) {
            return false
        }
        return true
    }

    //Request
    private fun addList(title: String) {
        var id: Int = 1
        if (lista.size > 0) {
            id = lista.get(0).listId + 1
        }
        val newList = TaskListEntity(id, title, 1)
        lista.add(newList)
        addListToDB(newList)
        updateRV()
        showHideMessage()
        clearText()
        Toast.makeText(context, "Guardado", Toast.LENGTH_SHORT).show()
    }


    //DataBase
    private fun addListToDB(lista: TaskListEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            DataBaseRepository.getInstance(requireContext()).databaseDao()
                .addList(lista)
        }
    }

    private fun deleteList(item: TaskListEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            val list = item
            DataBaseRepository.getInstance(requireContext()).databaseDao()
                .deleteTaskList(list)
//            DataBaseRepository.getInstance(requireContext()).databaseDao()
//                .deleteTaskFromTaskList(list.listId)
            lista.remove(item)
            withContext(Dispatchers.Main) {
                updateRV()
            }
        }
    }

    fun getAllMyLists() {
        lifecycleScope.launch(Dispatchers.IO) {
            lista.clear()
            lista.addAll(
                DataBaseRepository.getInstance(requireContext()).databaseDao().findUserLists(userId)
            )
            //Más nueva a antigua
            lista.sortByDescending { it.listId }
            withContext(Dispatchers.Main) {
                updateRV()
            }
        }
    }

    //Fragment navigation
    private fun viewChange(listId: Int, listName: String) {
        val action =
            TaskListFragmentDirections.actionTaskListFragmentToTasksFragment(listId, listName)
        findNavController().navigate(action)
    }
}