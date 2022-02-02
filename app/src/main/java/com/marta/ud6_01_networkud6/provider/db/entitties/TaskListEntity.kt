package com.marta.ud6_01_networkud6.provider.db.entitties

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marta.ud6_01_networkud6.model.TaskList

@Entity(tableName = "tasklist")
class TaskListEntity(
    @PrimaryKey
    @ColumnInfo(name = "listd")
    val listId: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "userFk")
    val userFk: Int
)
fun TaskList.toEntityTaskList(): TaskListEntity = TaskListEntity(listId,name, userFk)
fun List<TaskList>.toListOfEntityList(): List<TaskListEntity> =map { it.toEntityTaskList() }