package com.marta.ud6_01_networkud6.model

import com.google.gson.annotations.SerializedName
import com.marta.ud6_01_networkud6.provider.db.entitties.TaskEntity
import com.marta.ud6_01_networkud6.provider.db.entitties.TaskListEntity

data class Task(
    @SerializedName("taskId")
    val taskId: Int,
    @SerializedName("listIdFk")
    val listIdFk: Int,
    val description: String,
    val state: String,
    val title: String
)
fun Task.toEntityTask(): TaskEntity = TaskEntity(taskId,listIdFk, description, state,title)
fun List<Task>.toTaskFromTaskEntity(): List<TaskEntity> = map { it.toEntityTask()}