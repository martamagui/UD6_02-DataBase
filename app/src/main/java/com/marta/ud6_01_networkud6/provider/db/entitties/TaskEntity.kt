package com.marta.ud6_01_networkud6.provider.db.entitties

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
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
    val state: String,
    @ColumnInfo(name = "title")
    val title: String
)

