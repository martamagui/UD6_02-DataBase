package com.marta.ud6_01_networkud6.provider.db.entitties

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
class UserEntity(
    @PrimaryKey
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