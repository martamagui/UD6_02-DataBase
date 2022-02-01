package com.marta.ud6_01_networkud6.provider.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marta.ud6_01_networkud6.provider.db.entitties.TaskEntity
import com.marta.ud6_01_networkud6.provider.db.entitties.TaskListEntity
import com.marta.ud6_01_networkud6.provider.db.entitties.UserEntity

@Database(entities = [TaskListEntity::class, TaskEntity::class, UserEntity::class], version = 1)
abstract class AppDataBase: RoomDatabase(){
    abstract fun databaseDao(): DataBaseDao
}