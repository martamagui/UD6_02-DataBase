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
import com.marta.ud6_01_networkud6.databinding.FragmentAddTaskBinding
import com.marta.ud6_01_networkud6.provider.db.DataBaseRepository
import com.marta.ud6_01_networkud6.provider.db.entitties.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class AddTaskFragment : Fragment() {
    private var _binding: FragmentAddTaskBinding? = null
    private val binding
        get() = _binding!!
    private val args: AddTaskFragmentArgs by navArgs()
    private var newId = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener {
            if(checkTV()){
                val title: String = binding.etTitleTask.text.toString()
                val description : String = binding.etDescriptionTask.text.toString()
                val task = TaskEntity(null, args.listId,description,"Pendiente",title)
                addTask(task)
            }
        }
    }
    //Check
    private fun checkTV():Boolean{
        if(binding.etTitleTask.text.toString().count()>0){
            return true
        }
        Toast.makeText(context,"Rellena los campos",Toast.LENGTH_SHORT).show()
        return false;
    }
    private fun clearTxtFiles(){
        with(binding){
            etTitleTask.setText("")
            etDescriptionTask.setText("")
        }
    }

    //Api
    fun addTask(task: TaskEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            DataBaseRepository.getInstance(requireContext()).databaseDao().addTask(task)
            withContext(Dispatchers.Main){
                clearTxtFiles()
                Toast.makeText(context,"Guardado",Toast.LENGTH_SHORT).show()
            }
        }
    }
}