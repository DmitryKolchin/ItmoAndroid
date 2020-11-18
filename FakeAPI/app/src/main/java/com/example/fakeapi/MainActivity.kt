package com.example.fakeapi

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (MyApp.instance.posts.isNotEmpty()){
            fillRecyclerView()
        }
        else {
            MyApp.instance.APIService.loadPosts().enqueue(MyPostsListCallback())
        }
    }
    inner class MyPostCallback : Callback<Post> {
        override fun onFailure(p0: Call<Post>?, p1: Throwable?) {
            Toast.makeText(this@MainActivity, "Oops, some troubles with connection occurred while accessing a post", Toast.LENGTH_SHORT).show()
        }

        override fun onResponse(p0: Call<Post>?, p1: Response<Post>?) {
            if (p1 != null) {
                Toast.makeText(this@MainActivity, "Request code: " + p1.code().toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
    inner class MyPostsListCallback : Callback<List<Post>> {
        override fun onFailure(p0: Call<List<Post>>, p1: Throwable) {
            Toast.makeText(this@MainActivity, "Oops, some troubles with connection occurred while getting a list", Toast.LENGTH_SHORT).show()
        }
        override fun onResponse(p0: Call<List<Post>>, p1: Response<List<Post>>) {
            Log.i("DownloadInfo", "ListDownloaded")
            MyApp.instance.posts = p1.body()
            fillRecyclerView()
        }
    }
    fun fillRecyclerView(){
        myRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = PostsAdapter(MyApp.instance.posts) {
                MyApp.instance.APIService.deletePost(it.id.toString()).enqueue(MyPostCallback())
            }
        }
        progressBar.visibility = View.INVISIBLE

    }
    fun onAddClickEvent(view: View){
        if (view is Button){
            var post = Post(2, 2, "2", "2")
            MyApp.instance.APIService.createPost(post).enqueue(MyPostCallback())
        }
    }
}