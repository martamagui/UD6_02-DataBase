package com.marta.ud6_01_networkud6.provider.db.entitties

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user", indices = [Index(value = ["userId"], unique = true)])
class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userId")
    val userId: Int,
    @ColumnInfo(name = "firstName")
    val firstName: String,
    @ColumnInfo(name = "lastName")
    val lastName: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "pwd")
    val pwd: String,
    @ColumnInfo(name = "remember")
    val remember: Boolean
)