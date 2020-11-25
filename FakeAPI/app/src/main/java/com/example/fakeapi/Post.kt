package com.example.fakeapi

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Post(
    @ColumnInfo(name = "user_id") val userId : Int?,
    @PrimaryKey val id : Int?,
    @ColumnInfo(name =  "title") val title : String?,
    @ColumnInfo(name = "body") val body : String?) {
}