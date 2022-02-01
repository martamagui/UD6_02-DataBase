package com.marta.ud6_01_networkud6.provider.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.marta.ud6_01_networkud6.model.Task
import com.marta.ud6_01_networkud6.provider.db.entitties.TaskEntity
import com.marta.ud6_01_networkud6.provider.db.entitties.TaskListEntity
import com.marta.ud6_01_networkud6.provider.db.entitties.UserEntity

@Dao
interface DataBaseDao {
    //SELECT
    @Query("SELECT * FROM user WHERE email= :email AND pwd= :pwd")
    fun findUser(email: String, pwd: String): UserEntity

    @Query("SELECT *  FROM tasklist WHERE userFk=:userId")
    fun findUserLists(userId: Int): List<TaskListEntity>

    @Query("SELECT *  FROM tasks WHERE listIdFk=:listIdFk")
    fun findTaskFromList(listIdFk: Int): List<Task>

    //Inserts
    @Insert
    suspend fun addUser(user: UserEntity)

    @Insert
    fun createList(list: TaskListEntity)

    @Insert
    fun createTask(task: TaskEntity)

    //Delete
    @Delete
    fun deleteUser(user: UserEntity)

    @Delete
    fun deleteTaskList(list: TaskListEntity)

    @Delete
    fun deleteTask(task: TaskEntity)
}