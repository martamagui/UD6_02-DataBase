package com.marta.ud6_01_networkud6.usescases.common


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marta.ud6_01_networkud6.databinding.ItemTasklistBinding
import com.marta.ud6_01_networkud6.model.TaskList
import com.marta.ud6_01_networkud6.usescases.common.TaskListAdapter.ViewHolder


class TaskListAdapter(private val onListBinClicked: (TaskList) -> Unit, private val onListTitleClicked: (TaskList) -> Unit) :
    ListAdapter<TaskList, ViewHolder>(TaskListItemCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemTasklistBinding = ItemTasklistBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val taskList = getItem(position)
        with(holder.binding){
            tvListTitle.text = taskList.name
            tvListTitle.setOnClickListener{
                onListTitleClicked(taskList)
            }
            ivBinListitem.setOnClickListener {
                onListBinClicked(taskList)
            }
        }
    }

    inner class ViewHolder(val binding: ItemTasklistBinding) :
        RecyclerView.ViewHolder(binding.root)
}

class TaskListItemCallBack : DiffUtil.ItemCallback<TaskList>() {
    override fun areItemsTheSame(oldItem: TaskList, newItem: TaskList): Boolean {
        return oldItem.listId == newItem.listId
    }
    override fun areContentsTheSame(oldItem: TaskList, newItem: TaskList): Boolean {
        return oldItem.listId == newItem.listId
    }
}