package com.marta.ud6_01_networkud6.provider.db.entitties

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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