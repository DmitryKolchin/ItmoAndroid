package com.example.networking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_high_res.*
import java.io.ByteArrayOutputStream
import java.lang.ref.WeakReference
import java.net.URL


class HighResActivity : AppCompatActivity() {
    private val broadcastReceiver = PictureBroadcastReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_res)
        val intentDownloadImage = Intent(this, ImageDownloadService::class.java)
        intentDownloadImage.putExtra("url", intent.extras?.getString("url"))
        startService(intentDownloadImage)
        val intentFilter = IntentFilter(
            "SEND IMAGE"
        )
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
        registerReceiver(broadcastReceiver, intentFilter)
        //ImageDownload(this).execute(intent.extras?.getString("url"))
    }
    private class ImageDownload (activity : HighResActivity) : AsyncTask<String, Unit, Bitmap>(){
        private val activityRef = WeakReference(activity)
        override fun doInBackground(vararg params: String?): Bitmap? {
            var bitmapImage : Bitmap? = null
            try {
                val inputStream = URL(params[0]).openStream()
                val byteStream = ByteArrayOutputStream();
                bitmapImage = BitmapFactory.decodeStream(inputStream)
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 3, byteStream)
            } catch (e : Exception) {
                e.printStackTrace()
            }
            return bitmapImage
        }

        override fun onPostExecute(result: Bitmap?) {
            activityRef.get()?.pasteImage(result)
        }

    }
    internal fun pasteImage(image : Bitmap?){
        if (image != null){
            imageView.setImageBitmap(image)
        }
    }
    inner class PictureBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val picture = intent.extras?.get("Picture")
            if (picture is ByteArray){
                pasteImage(BitmapFactory.decodeByteArray(picture, 0, picture.size))
            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }
}