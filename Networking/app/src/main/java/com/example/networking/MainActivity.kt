package com.example.networking

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.AsyncTask
import android.os.Bundle
import android.os.IBinder
import android.os.Messenger
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.URL


class MainActivity : AppCompatActivity() {
    companion object {
        const val MESSAGE = "MESSAGE"
    }
    private lateinit var label : TextView
    private var message : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        destroyDownloadingService()
        message = savedInstanceState?.getString(MESSAGE)
        setContentView(R.layout.activity_main)
        if (message == null){
            ImageListDownload(this).execute("")
            Log.i("downloading", "DownloadingImageList")
        }
        else {
            fillRecyclerView(message.toString())
        }
    }
    fun destroyDownloadingService(){
        val intentDownloadImage = Intent(this, ImageDownloadingService::class.java)
        stopService(intentDownloadImage)
    }

    override fun onResume() {
        super.onResume()
        destroyDownloadingService()
    }
    private class ImageListDownload(activity: MainActivity) : AsyncTask<String, Unit, String>(){
        private val activityRef = WeakReference(activity)
        override fun doInBackground(vararg params: String?): String? {
            var imageList : List<Image>? = null
            var result : String? = null
            val url = "https://picsum.photos/v2/list?limit=10"
            try {
                InputStreamReader(URL(url).openConnection().getInputStream()).use {
                    result = it.readText()
                }
            } catch (e: IOException){
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            val activity = activityRef.get()
            if (result != null)
                activity?.fillRecyclerView(result)
        }
    }
    fun fillRecyclerView(string: String){
        message = string
        val imageList : List<Image> = Gson().fromJson(message,Array<Image>::class.java).toList()
        val viewManager = LinearLayoutManager(this)
        MyRecyclerView.apply {
            layoutManager = viewManager
            adapter = ImagesAdapter(imageList) {
                val intent = Intent(this@MainActivity, HighResActivity::class.java)
                intent.putExtra("url", (it.download_url))
                startActivity(intent)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(MESSAGE, message)
        super.onSaveInstanceState(outState)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        message = savedInstanceState.getString(MESSAGE)
    }
}
