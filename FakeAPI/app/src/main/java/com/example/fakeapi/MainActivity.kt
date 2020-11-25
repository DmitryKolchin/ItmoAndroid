package com.example.fakeapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Database
import androidx.room.RoomDatabase
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    @Database(entities = [Post::class], version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun postsDao(): PostsDao?
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val arguments = intent.extras
        if (arguments != null && !arguments.isEmpty){
            MyApp.instance.post?.let { addPost(it) }
        }

        if (MyApp.instance.postsDao?.getAll()?.isNotEmpty()!!){
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
                Toast.makeText(this@MainActivity, "Request code: " + p1.code().toString() , Toast.LENGTH_SHORT).show()
            }
        }
    }
    inner class MyPostsListCallback : Callback<List<Post>> {
        override fun onFailure(p0: Call<List<Post>>, p1: Throwable) {
            Toast.makeText(this@MainActivity, "Oops, some troubles with connection occurred while getting a list", Toast.LENGTH_SHORT).show()
            myRecyclerView.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }
        override fun onResponse(p0: Call<List<Post>>, p1: Response<List<Post>>) {
            Log.i("DownloadInfo", "ListDownloaded")
            val posts = p1.body()
            if (posts != null) {
                MyApp.instance.clearDatabase()
                for (post in posts) {
                    MyApp.instance.postsDao?.insertPost(post)
                }
            }
            fillRecyclerView()
        }
    }
    fun fillRecyclerView(){
        myRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = MyApp.instance.postsDao?.getAll()?.let {
                PostsAdapter(it) {
                    MyApp.instance.APIService.deletePost(it.id.toString()).enqueue(MyPostCallback())
                    MyApp.instance.postsDao?.deletePost(it)
                    fillRecyclerView()
                }
            }
        }
        myRecyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE

    }
    fun onAddClickEvent(view: View){
        Log.i("sas", "asa")
        val intent = Intent(this@MainActivity, AddPostActivity::class.java)
        startActivity(intent)
    }
    private fun addPost(post : Post){
        MyApp.instance.APIService.createPost(post).enqueue(MyPostCallback())
        MyApp.instance.postsDao?.insertPost(post);
        fillRecyclerView()
    }
    fun onReloadClickEvent(view: View){
        myRecyclerView.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
        MyApp.instance.APIService.loadPosts().enqueue(MyPostsListCallback())



    }
}