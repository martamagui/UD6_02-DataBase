package com.marta.ud6_01_networkud6.provider.db

import androidx.room.*
import com.marta.ud6_01_networkud6.provider.db.entitties.ListWithTasks
import com.marta.ud6_01_networkud6.provider.db.entitties.TaskEntity
import com.marta.ud6_01_networkud6.provider.db.entitties.TaskListEntity
import com.marta.ud6_01_networkud6.provider.db.entitties.UserEntity
import retrofit2.http.DELETE

@Dao
interface DataBaseDao {
    //SELECT
    @Query("SELECT * FROM user WHERE email= :email AND pwd= :pwd")
    suspend fun findUser(email: String, pwd: String): UserEntity

    @Query("SELECT *  FROM tasklist WHERE userFk=:userId")
    suspend fun findUserLists(userId: Int): List<TaskListEntity>

    @Transaction
    @Query("SELECT *  FROM taskList WHERE listId=:listIdFk")
    suspend fun findTaskFromList(listIdFk: Int): ListWithTasks

    @Query("SELECT * FROM tasks WHERE taskId=:taskId")
    suspend fun findTaskById(taskId: Int): TaskEntity


    //Inserts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addList(list: TaskListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllLists(lists: List<TaskListEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllTask(tasks: List<TaskEntity>)

    //Upddate
    @Update
    suspend fun updateTask(task: TaskEntity)

    //Delete
    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Delete
    suspend fun deleteTaskList(list: TaskListEntity)

    @Query("DELETE FROM tasklist WHERE listId=:listId")
    suspend fun deleteTaskListById(listId: Int)

    @Query("DELETE FROM tasks WHERE listIdFk=:listId")
    suspend fun deleteTaskByFK(listId: Int)

    suspend fun deleteListAndItsTasks(listId: Int){
        deleteTaskListById(listId)
        deleteTaskByFK(listId)
    }

    @Delete
    suspend fun deleteTask(task: TaskEntity)

}