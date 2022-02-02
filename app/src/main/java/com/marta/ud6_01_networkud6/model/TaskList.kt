package com.marta.ud6_01_networkud6.model

import com.marta.ud6_01_networkud6.provider.db.entitties.TaskListEntity

data class TaskList(
    val listId: Int,
    val name: String,
    val userFk: Int
)
fun TaskList.toEntityTaskList(): TaskListEntity = TaskListEntity(listId,name, userFk)
fun List<TaskList>.toListOfEntityList(): List<TaskListEntity> =map { it.toEntityTaskList() }