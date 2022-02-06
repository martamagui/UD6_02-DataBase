package com.marta.ud6_01_networkud6.provider.db.entitties

import androidx.room.Embedded
import androidx.room.Relation

data class ListWithTasks(
    @Embedded val list: TaskListEntity,
    @Relation(
        parentColumn = "listId",
        entityColumn = "listIdFk"
    )
    val tasks : List<TaskEntity>?
)
