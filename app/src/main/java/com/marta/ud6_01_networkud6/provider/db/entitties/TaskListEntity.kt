package com.marta.ud6_01_networkud6.provider.db.entitties

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.marta.ud6_01_networkud6.model.TaskList

@Entity(tableName = "tasklist", indices = [Index(value = ["listId"], unique = true)])
data class TaskListEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "listId")
    val listId: Int,
    @ColumnInfo(name = "name")
    val name: String,
    val date:String,
    val priority: Int,
    @ColumnInfo(name = "userFk")
    val userFk: Int
)
