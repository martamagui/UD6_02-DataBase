package com.marta.ud6_01_networkud6.provider.db.entitties

import androidx.room.*

@Entity(tableName = "user", indices = [Index(value = ["userId"], unique = true)])
data class UserEntity(
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