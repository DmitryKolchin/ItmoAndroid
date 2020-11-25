package com.example.fakeapi

import androidx.room.*

@Dao
public interface PostsDao {
    @Query("SELECT * FROM post")
    fun getAll() : List<Post>

    @Delete
    fun deletePost(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(post: Post)

}