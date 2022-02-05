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
            val text = binding.tfNewList.text.toString()
            addList(text)
        }
        getAllMyLists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // UI
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

    private fun updateRV(toListOfList: List<TaskListEntity>) {
        adapter.submitList(lista)
        showHideMessage()
        adapter.notifyDataSetChanged()
    }

    //Fragment navigation
    private fun viewChange(listId: Int, listName: String) {
        val action =
            TaskListFragmentDirections.actionTaskListFragmentToTasksFragment(listId, listName)
        findNavController().navigate(action)
    }

    //Request
    private fun addList(title: String) {
        val newList: TaskList = TaskList((lista.get(0).listId + 1), title, 1)
        val service = TaskApi.service.addList(newList)
        val call = service.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    lista.add(newList.toEntityTaskList())
                    updateRV(lista)
                    addListToDB(newList)
                    showHideMessage()
                    clearText()
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "(╯°□°）╯︵ ┻━┻ Format faliure", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(context, "(╯°□°）╯︵ ┻━┻ Connection faliure ", Toast.LENGTH_SHORT)
                    .show()
                Log.e("faliure", "$t")
            }
        })
    }

    private fun deleteList(list: TaskListEntity) {
        val service = TaskApi.service.deleteList(list.listId)
        val call = service.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (!response.isSuccessful) {
                    Log.d("Item", "(╯°□°）╯︵ ┻━┻ Formato incorrecto")
                    lista.remove(list)
                    deleteListInDB(list)
                    updateRV(lista)
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                } else {
                }
            }
            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.d("Item", "(╯°□°）╯︵ ┻━┻ Formato incorrecto $t")
            }
        })

    }

    //DataBase
    private fun addListToDB(lista: TaskList) {
        lifecycleScope.launch(Dispatchers.IO) {
            DataBaseRepository.getInstance(requireContext()).databaseDao()
                .addList(lista.toEntityTaskList())
        }
    }

    private fun deleteListInDB(lista: TaskListEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            val list = lista
            DataBaseRepository.getInstance(requireContext()).databaseDao()
                .deleteTaskList(list)
            DataBaseRepository.getInstance(requireContext()).databaseDao().deleteTaskFromTaskList(list.listId)
        }
    }

    private fun findListsInDB() {
        lifecycleScope.launch(Dispatchers.IO) {
            val lists: List<TaskListEntity> =
                DataBaseRepository.getInstance(requireContext()).databaseDao().findAllLists()
            updateRV(lists)
        }
    }

    fun getAllMyLists() {
        lifecycleScope.launch(Dispatchers.IO) {
            lista.addAll(DataBaseRepository.getInstance(requireContext()).databaseDao().findUserLists(userId))
            //Más nueva a antigua
            lista.sortByDescending { it.listId }
            updateRV(lista)
        }
    }
}