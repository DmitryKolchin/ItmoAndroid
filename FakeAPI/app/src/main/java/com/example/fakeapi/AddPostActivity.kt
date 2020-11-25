package com.example.fakeapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_post.*


class AddPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
    }
    fun addClickEvent(view: View){
        if (userIdEdit.text.toString() == "" || postIdEdit.text.toString() == "" || postTitleEdit.text.toString() == ""){
            Toast.makeText(this@AddPostActivity, "Oops, you missed some data", Toast.LENGTH_SHORT).show()
            return
        }
        val postUserId = userIdEdit.text.toString().toInt()
        Log.i("sas", postIdEdit.text.toString())
        val postId = postIdEdit.text.toString().toInt()
        val postTitle = postTitleEdit.text.toString()
        val postBody = postBodyEdit.text.toString()
        var post = Post(postUserId, postId, postTitle, postBody)
        MyApp.instance.post = post
        val intent = Intent(this@AddPostActivity, MainActivity::class.java)
        intent.putExtra("added", true)
        startActivity(intent)
    }
    fun backClickEvent(view: View){
        val intent = Intent(this@AddPostActivity, MainActivity::class.java)
        startActivity(intent)
    }
}