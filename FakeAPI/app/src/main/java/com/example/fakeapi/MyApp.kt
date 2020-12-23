package com.example.fakeapi

import android.app.Application
import android.os.AsyncTask
import androidx.room.Room
import com.example.fakeapi.MainActivity.AppDatabase
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class MyApp() : Application() {
    private val BASE_URL = "https://jsonplaceholder.typicode.com"
    companion object {
        lateinit var instance: MyApp
            private set
    }
    lateinit var retrofit : Retrofit
    lateinit var APIService : FakeAPIService
    var database: AppDatabase? = null
    var postsDao : PostsDao? = null
    var post : Post? = null;
    override fun onCreate() {
        super.onCreate()
        instance = this
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        APIService = retrofit.create(FakeAPIService::class.java)
        database = Room.databaseBuilder(this@MyApp, AppDatabase::class.java, "posts-database").build()
        postsDao = database?.postsDao()
    }
    fun clearDatabase(){
        val posts = postsDao?.getAll()
        if (posts != null){
            for (post in posts){
                postsDao?.deletePost(post)
            }
        }
    }

}