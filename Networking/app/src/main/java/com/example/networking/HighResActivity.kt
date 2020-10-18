package com.example.networking

import android.app.PendingIntent
import android.content.*
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_high_res.*


class HighResActivity : AppCompatActivity() {
    private var sConn: ServiceConnection? = null
    var bound = false
    var myService: ImageDownloadingService? = null
    private val broadcastReceiver = PictureBroadcastReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_res)
        val intentDownloadImage = Intent(this, ImageDownloadingService::class.java)
        intentDownloadImage.putExtra("url", intent.extras?.getString("url"))
        sConn = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, binder: IBinder) {
                bound = true
                myService = (binder as ImageDownloadingService.MyBinder).getService()
            }
            override fun onServiceDisconnected(name: ComponentName) {
                bound = false
            }
        }
        startService(intentDownloadImage)
        bindService(intentDownloadImage, sConn as ServiceConnection, 0)
        val intentFilter = IntentFilter(
            "SEND IMAGE"
        )
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
        registerReceiver(broadcastReceiver, intentFilter)

    }
    inner class PictureBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            pasteImage(myService?.bitmapImage)
        }
    }
    private fun pasteImage(image : Bitmap?){
        if (image != null){
            imageView.setImageBitmap(image)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
        if (bound){
            sConn?.let { unbindService(it) };
        }
    }
}