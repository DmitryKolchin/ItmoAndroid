package com.example.fakeapi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class MainActivity : AppCompatActivity() {
    val BASE_URL = "https://jsonplaceholder.typicode.com"
    inner class MyCallback : Callback<List<Post>>{
        override fun onFailure(p0: Call<List<Post>>, p1: Throwable) {
            TODO("Not yet implemented")
        }

        override fun onResponse(p0: Call<List<Post>>, p1: Response<List<Post>>) {
            for (post in p1.body()){
                Log.i("mmm",  post.id.toString())
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        var APIService = retrofit.create(FakeAPIService::class.java)
        val loadPosts = APIService.loadPosts()
    }
}