package com.marta.ud6_01_networkud6.provider.db.entitties

import androidx.room.*
import com.google.gson.annotations.SerializedName


@Entity(tableName = "tasks", indices = [Index(value = ["taskId"], unique = true)])
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "taskId")
    val taskId: Int?,
    @ColumnInfo(name = "listIdFk")
    val listIdFk: Int,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "state")
    var state: String,
    @ColumnInfo(name = "title")
    val title: String
)

