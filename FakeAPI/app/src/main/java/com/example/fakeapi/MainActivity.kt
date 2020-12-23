package com.example.fakeapi

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
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

    lateinit var posts: List<Post>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val arguments = intent.extras
        if (arguments != null && !arguments.isEmpty){
            MyApp.instance.post?.let { addPost(it) }
        }
        GetAllPostsAsync()
    }
    private inner class DoAsync(private val handler: () -> Unit) : AsyncTask<Unit, Unit, Unit>() {
        init {
            Log.i("sas", "saaas")
            execute()
        }

        override fun doInBackground(vararg params: Unit) {
            handler()
        }

        override fun onPostExecute(result: Unit?) {
            GetAllPostsAsync()
            super.onPostExecute(result)
        }
    }
    private inner class GetAllPostsAsync() : AsyncTask<Unit, Unit, List<Post>>() {
        init {
            execute()
        }
        override fun doInBackground(vararg params: Unit): List<Post>? {
            return MyApp.instance.postsDao?.getAll()
        }

        override fun onPostExecute(result: List<Post>) {
            if (result.isNotEmpty()) {
                progressBar?.visibility = ProgressBar.INVISIBLE
                posts = result
                fillRecyclerView()
            }
            else {
                MyApp.instance.APIService.loadPosts().enqueue(MyPostsListCallback())
            }

            super.onPostExecute(result)
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
            val downloadedPosts = p1.body()
            if (posts != null) {
                DoAsync {
                    MyApp.instance.clearDatabase()
                }
                for (post in downloadedPosts) {
                    DoAsync{
                        MyApp.instance.postsDao?.insertPost(post)
                    }

                }
            }
            GetAllPostsAsync()
        }
    }
    fun fillRecyclerView(){
        myRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = PostsAdapter(posts) {
                MyApp.instance.APIService.deletePost(it.id.toString()).enqueue(MyPostCallback())
                DoAsync {
                    MyApp.instance.postsDao?.deletePost(it)
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
        DoAsync {
            MyApp.instance.postsDao?.insertPost(post);
        }
    }
    fun onReloadClickEvent(view: View){
        myRecyclerView.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
        MyApp.instance.APIService.loadPosts().enqueue(MyPostsListCallback())
        GetAllPostsAsync()



    }
}