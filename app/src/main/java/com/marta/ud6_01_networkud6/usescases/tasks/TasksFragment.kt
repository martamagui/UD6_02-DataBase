package com.marta.ud6_01_networkud6.usescases.tasks;

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.marta.ud6_01_networkud6.databinding.FragmentTasksBinding
import com.marta.ud6_01_networkud6.provider.db.DataBaseRepository
import com.marta.ud6_01_networkud6.provider.db.entitties.ListWithTasks
import com.marta.ud6_01_networkud6.provider.db.entitties.TaskEntity
import com.marta.ud6_01_networkud6.provider.db.entitties.TaskListEntity
import com.marta.ud6_01_networkud6.usescases.common.TaskAdapter
import kotlinx.coroutines.*


class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding? = null
    private val binding
        get() = _binding!!
    private val args: TasksFragmentArgs by navArgs()
    private var listId: Int = 0
    private var editMode = false
    private val adapter = TaskAdapter({toDetailView(it)}){
        cambiarEstado(it)
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
        listId = args.listIdFk
        setUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //UI
    private fun setUI() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val listWithTasks = getListWithTasksFromDB()
            //jeje
            delay(200)
            hideShowProgressBar()
            getTasks(listWithTasks)
            setInUIListInformation(listWithTasks.list)
        }
        setAdapter()
        disableEditUI()
        binding.fabAddTask.setOnClickListener {
            viewChangeAddTaskView(args.listIdFk)
        }
        binding.ibEdit.setOnClickListener {
            enableDiableEditMode()
        }
        binding.ivBin.setOnClickListener {
            deleteList(listId)
        }
        binding.ivSave.setOnClickListener {
            enableDiableEditMode()
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                val editedList = createEditedList()
                editListDB(editedList)
                setInUIListInformation(editedList)
            }
            Toast.makeText(context,"Cambios aplicados", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setInUIListInformation(list: TaskListEntity) {
        with(binding) {
            tvListTitle.text = list.name
            etTitle.setText(list.name)
            tvListDescription.text = list.description
            tvPriority.text = "Prio. : ${list.priority.toString()}"
            spinnerPriority.setSelection(list.priority - 1)
            tvListDate.text = list.date
        }

    }

    private fun setAdapter() {
        binding.rvTasks.adapter = adapter
        binding.rvTasks.layoutManager = LinearLayoutManager(context)
    }

    private fun showHideMessage(listWithTasks: List<TaskEntity>?) {
        if (!listWithTasks.isNullOrEmpty()) {
            binding.tvNoTask.visibility = View.INVISIBLE
        } else {
            binding.tvNoTask.visibility = View.VISIBLE
        }
    }
    private fun hideShowProgressBar(){
        with(binding){
            if(progressBarTask.visibility==View.INVISIBLE){
                progressBarTask.visibility=View.VISIBLE
            }else{
                progressBarTask.visibility=View.INVISIBLE
            }
        }
    }
    private fun updateRV(listWithTasks: ListWithTasks) {
        if (listWithTasks.tasks?.size ?: -1 > 0) {
            var list = listWithTasks.tasks!!
            adapter.submitList(list)
        }
        showHideMessage(listWithTasks.tasks)
    }

    private fun deletedListview() {
        binding.tvListTitle.text = "Eliminado"
        binding.fabAddTask.isEnabled = false
        binding.ivBin.visibility = View.GONE
        binding.rvTasks.visibility = View.GONE
        binding.ibEdit.visibility = View.GONE
    }

    private fun disableEditUI() {
        with(binding) {
            tvListTitle.visibility = View.VISIBLE
            tvListDescription.visibility = View.VISIBLE
            tvPriority.visibility = View.VISIBLE
            ivBin.visibility = View.VISIBLE
            ibEdit.visibility = View.VISIBLE
            ivBin.isEnabled = true
            ibEdit.isEnabled = true
            etTitle.visibility = View.INVISIBLE
            etDescription.visibility = View.INVISIBLE
            spinnerPriority.visibility = View.INVISIBLE
            ivSave.visibility = View.INVISIBLE
            ivSave.isEnabled = false

        }
    }

    private fun enableEditUI() {
        with(binding) {
            tvListTitle.visibility = View.INVISIBLE
            tvListDescription.visibility = View.INVISIBLE
            tvPriority.visibility = View.INVISIBLE
            ivBin.visibility = View.INVISIBLE
            ibEdit.visibility = View.INVISIBLE
            ivBin.isEnabled = false
            ibEdit.isEnabled = false
            etTitle.visibility = View.VISIBLE
            etDescription.visibility = View.VISIBLE
            spinnerPriority.visibility = View.VISIBLE
            ivSave.visibility = View.VISIBLE
            ivSave.isEnabled = true
        }
    }

    //DB
    private suspend fun getListWithTasksFromDB(): ListWithTasks {
        val listAndTasks = DataBaseRepository.getInstance(requireContext()).databaseDao()
            .findTaskFromList(listId)
        return listAndTasks
    }

    private fun deleteList(id: Int) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            DataBaseRepository.getInstance(requireContext()).databaseDao().deleteListAndItsTasks(id)
            withContext(Dispatchers.Main) {
                deletedListview()
            }
        }
    }

    private suspend fun editListDB(list: TaskListEntity) {
        DataBaseRepository.getInstance(requireContext()).databaseDao().updateList(list)
    }

    private suspend fun editTask(task: TaskEntity){
        DataBaseRepository.getInstance(requireContext()).databaseDao().updateTask(task)
    }

    //Utils
    private fun getTasks(listWithTasks: ListWithTasks) {
        if (listWithTasks?.tasks?.count()!! > 0) {
            updateRV(listWithTasks)
        }
    }

    private fun enableDiableEditMode() {
        if (editMode) {
            disableEditUI()
            editMode = false
        } else {
            enableEditUI()
            editMode = true
        }
    }

    private suspend fun createEditedList(): TaskListEntity {
        var listToUpdate: TaskListEntity = getListWithTasksFromDB().list
        listToUpdate.name = binding.etTitle.text.toString()
        listToUpdate.description = binding.etDescription.text.toString()
        listToUpdate.priority = binding.spinnerPriority.selectedItemPosition + 1
        return listToUpdate
    }


    //Navigation and Item actions
    private fun toDetailView(task: TaskEntity) {
        val action = TasksFragmentDirections.actionTasksFragmentToDetailTaskFragment(task.taskId!!)
        findNavController().navigate(action)
    }

    private fun viewChangeAddTaskView(listId: Int) {
        val action = TasksFragmentDirections.actionTasksFragmentToAddTaskFragment(listId)
        findNavController().navigate(action)
    }
    private fun cambiarEstado(task: TaskEntity){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            var taskToUpdate= task
            if(task.state == "Pendiente"){
                taskToUpdate.state = "Terminada"
            }else{
                taskToUpdate.state = "Pendiente"
            }
            editTask(taskToUpdate)
        }
    }

}