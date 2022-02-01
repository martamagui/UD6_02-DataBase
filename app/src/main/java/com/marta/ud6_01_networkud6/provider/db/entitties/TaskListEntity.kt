package com.marta.ud6_01_networkud6.provider.db.entitties

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TaskList")
class TaskListEntity(
    @PrimaryKey
    @ColumnInfo(name = "listId")
    val listId: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "listId")
    val userFk: Int
)