package com.marta.ud6_01_networkud6.model

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("taskId")
    val taskId: Int,
    @SerializedName("listIdFk")
    val listIdFk: Int,
    val description: String,
    val state: String,
    val title: String
)