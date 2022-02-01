package com.marta.ud6_01_networkud6.provider.db

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DataBaseRepository {
    private var INSTANCE: AppDataBase? = null

    fun getInstance(context: Context): AppDataBase = INSTANCE ?: synchronized(this) {
        INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
    }
    private fun buildDatabase(context: Context) =
        Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, "AppDataBase")
            .allowMainThreadQueries()
            .build()
}