package com.marta.ud6_01_networkud6.provider.api

import com.marta.ud6_01_networkud6.model.Task
import com.marta.ud6_01_networkud6.model.TaskList
import retrofit2.Call
import retrofit2.http.*

interface TaskApiService {
    //Lists
    @GET("api/list")
    fun getTaskLists(): Call<List<TaskList>>

    @POST("api/list")
    fun addList(@Body taskList: TaskList): Call<Any>

    @DELETE("api/list/{listId}")
    fun deleteList(@Path("listId") listId: Int): Call<Int>

    //Tasks
    @GET("api/tasks")
    fun getTasks(): Call<List<Task>>

    @GET("api/tasks/{taskId}")
    fun getTaskById(@Path("taskId") taskId: Int): Call<Task>

    @GET("api/tasks/bylistId/{listId}")
    fun getTaskByListId(@Path("listId") listId: Int): Call<List<Task>>

    @GET("api/tasks/getmyTaskId")
    fun getMyId():Call<Int>

    @POST("api/tasks")
    fun addTask(@Body task: Task): Call<Boolean>

    @PUT("api/tasks/{taskId}")
    fun editTask(
        @Path("taskId") taskId: Int,
        @Query("title") title: String,
        @Query("description") description: String
    ): Call<Task>

    @DELETE("api/tasks/{taskId}")
    fun deleteTask(@Path("taskId") listId: Int): Call<Any>
}