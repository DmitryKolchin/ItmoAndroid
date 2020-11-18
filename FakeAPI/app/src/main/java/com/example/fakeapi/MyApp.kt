package com.example.fakeapi

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.internal.Internal.instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
    var posts : List<Post> = listOf()
    override fun onCreate() {
        super.onCreate()
        instance = this
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        APIService = retrofit.create(FakeAPIService::class.java)
    }

}