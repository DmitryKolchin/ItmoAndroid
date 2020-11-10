package com.example.animations

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var label : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val set = AnimatorInflater.loadAnimator(this, R.animator.text_animator) as AnimatorSet
        label = findViewById(R.id.textView)
        set.setTarget(label)
        set.start()
    }
}