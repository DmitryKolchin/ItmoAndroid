package com.example.fakeapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST


interface FakeAPIService {
    @GET("/posts")
    fun loadPosts(): Call<List<Post>>
}