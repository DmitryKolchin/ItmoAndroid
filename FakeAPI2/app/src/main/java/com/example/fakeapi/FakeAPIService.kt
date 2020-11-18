package com.example.fakeapi

import retrofit2.Call
import retrofit2.http.*

interface FakeAPIService {
    @GET("/posts")
    fun loadPosts(): Call<List<Post>>
    @DELETE("/posts/{id}")
    fun deletePost(@Path("id") id : String) : Call<Post>
    @POST("/posts")
    fun  createPost(@Body post: Post) : Call<Post>
}