package com.marta.ud6_01_networkud6.usescases.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.marta.ud6_01_networkud6.databinding.ItemTaskBinding
import com.marta.ud6_01_networkud6.model.Task
import androidx.recyclerview.widget.ListAdapter
import com.marta.ud6_01_networkud6.provider.db.entitties.TaskEntity


class TaskAdapter(private val onItemClicked: (TaskEntity) -> Unit,  private val onCheckBoxClicked: (TaskEntity) -> Unit) : ListAdapter<TaskEntity, TaskAdapter.ViewHolderTask>(TaskItemCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.ViewHolderTask {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemTaskBinding  = ItemTaskBinding.inflate(inflater, parent, false)
        return ViewHolderTask(binding)
    }

    override fun onBindViewHolder(holder: TaskAdapter.ViewHolderTask, position: Int) {
        val task = getItem(position)
        with(holder.binding){
            tvTask.text = task.title
            if(task.state!="Pendiente"){
                cbState.isChecked = true
            }
            cbState.setOnClickListener{
                onCheckBoxClicked(task)
            }
            root.setOnClickListener { onItemClicked(task) }
        }
    }
    inner class ViewHolderTask( val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root)


}
class TaskItemCallBack: DiffUtil.ItemCallback<TaskEntity>(){
    override fun areItemsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
        return oldItem.taskId == newItem.taskId
    }
    override fun areContentsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
        return (oldItem.title == newItem.title && oldItem.description == newItem.description && oldItem.state == newItem.state )
    }
}