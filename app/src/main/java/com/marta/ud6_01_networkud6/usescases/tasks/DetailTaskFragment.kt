package com.marta.ud6_01_networkud6.usescases.tasks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.marta.ud6_01_networkud6.R
import com.marta.ud6_01_networkud6.databinding.FragmentDetailTaskBinding
import com.marta.ud6_01_networkud6.model.Task
import com.marta.ud6_01_networkud6.provider.api.TaskApi
import com.marta.ud6_01_networkud6.provider.db.DataBaseRepository
import com.marta.ud6_01_networkud6.provider.db.entitties.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response


class DetailTaskFragment : Fragment() {
    private var _binding: FragmentDetailTaskBinding? = null
    private val binding
        get() = _binding!!
    private val args: DetailTaskFragmentArgs by navArgs()
    private lateinit var task: TaskEntity
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
        val taskId = args.taskId
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
            deleteTaskById()
        }
        binding.btnSave.setOnClickListener {
            val description = binding.etDetailTaskDescription.text.toString()
            val title = binding.etDetailTaskTitle.text.toString()
            val editedTask = TaskEntity(taskId, task.listIdFk, description, "Pendiente", title)
            editTask(editedTask)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //UI
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

    private fun setTexts(task: TaskEntity) {
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
        lifecycleScope.launch(Dispatchers.IO) {
            task =
                DataBaseRepository.getInstance(requireContext()).databaseDao().findTaskById(taskId)
            withContext(Dispatchers.Main) {
                setTexts(task)
            }
        }
    }

    private fun deleteTaskById() {
        lifecycleScope.launch(Dispatchers.IO) {
            DataBaseRepository.getInstance(requireContext()).databaseDao().deleteTask(task)
            withContext(Dispatchers.Main) {
                viewTotaskWasDeleted()
            }
        }
    }

    private fun editTask(editedTask: TaskEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            DataBaseRepository.getInstance(requireContext()).databaseDao()
                .updateTask(editedTask)
            withContext(Dispatchers.Main) {
                setTexts(editedTask)
                changeToViewDetailMode()
            }
            task = editedTask
        }
    }
}