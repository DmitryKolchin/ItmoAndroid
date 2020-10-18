package com.example.networking

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.URL
import kotlin.math.log

class ImageDownloadingService : Service() {
    private val SERVICE_ACTION = "DOWNLOAD IMAGE"
    var binder = MyBinder()
    var bitmapImage : Bitmap? = null
    private var lastUrl : String? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }
    private class ImageDownload (activity : ImageDownloadingService) : AsyncTask<String, Unit, Bitmap>(){
        private val activityRef = WeakReference(activity)
        override fun doInBackground(vararg params: String?): Bitmap? {
            var bitmapImage : Bitmap? = null
            try {
                val inputStream = URL(params[0]).openStream()
                bitmapImage = BitmapFactory.decodeStream(inputStream)

            } catch (e : Exception) {
                e.printStackTrace()
            }
            return bitmapImage
        }

        override fun onPostExecute(result: Bitmap?) {
            Log.i("downloading", "downloading image")
            activityRef.get()?.bitmapImage = result
            val responseIntent = Intent()
            responseIntent.action = "SEND IMAGE"
            responseIntent.addCategory(Intent.CATEGORY_DEFAULT)
            activityRef.get()?.sendBroadcast(responseIntent)
        }

    }
    override fun onBind(intent: Intent): IBinder {
        val url = intent.getStringExtra("url")
        if (!url.equals(lastUrl)) {
            lastUrl = url
            ImageDownload(this).execute(url)
        }
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return true
    }

    override fun onRebind(intent: Intent?) {
        val url = intent?.getStringExtra("url")
        if (!url.equals(lastUrl)) {
            lastUrl = url
            ImageDownload(this).execute(url)
        }
        else {
            val responseIntent = Intent()
            responseIntent.action = "SEND IMAGE"
            responseIntent.addCategory(Intent.CATEGORY_DEFAULT)
            sendBroadcast(responseIntent)
        }
    }

    inner class MyBinder : Binder() {
        fun getService(): ImageDownloadingService {
            return this@ImageDownloadingService
        }
    }
}
