package com.example.networking

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.net.URL


class ImageDownloadService : IntentService("ImageDownloadService") {
    private var bitmapImage : Bitmap? = null
    private var last_url : String? = null
    override fun onHandleIntent(intent: Intent?) {
        val url = intent?.getStringExtra("url")
        if (!url.equals(last_url)){
            try {
                val inputStream = URL(url).openStream()
                bitmapImage = BitmapFactory.decodeStream(inputStream)
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
        val responseIntent = Intent()
        responseIntent.action = "SEND IMAGE"
        intent?.addCategory(Intent.CATEGORY_DEFAULT)
        responseIntent.putExtra("Picture", ByteArrayOutputStream().apply {
            bitmapImage?.compress(Bitmap.CompressFormat.JPEG, 100, this)
        }.toByteArray())
        sendBroadcast(responseIntent)
    }

}
