package com.example.animations

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.getSize

class CustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var paint = Paint().apply {
        color = 0xFFF111e6.toInt()
    }
    private var cx : Float = 0F
    private var cy : Float = 0F
    private var animSpeed : Float = 20f
    private var radius : Float = 100f
    private var xDirection : Int = 1
    private var yDirection : Int = 1
    private var mode : String = "y"

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(getSize(widthMeasureSpec), getSize(heightMeasureSpec))
        cx = measuredWidth.toFloat()/2
        cy = measuredHeight.toFloat()/2
    }
    init {
        val a: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.CustomView, defStyleAttr, defStyleRes)
        try {
            radius = a.getDimension(R.styleable.CustomView_customCircleRadius, 100f)
            mode = a.getString(R.styleable.CustomView_mode) ?: "x"
            animSpeed = a.getFloat(R.styleable.CustomView_ballSpeed, 20f)
        } finally {
            a.recycle()
        }
    }

    private fun changeCoordinate (coordinate : Float, coefficient : Int, border : Float) : Pair<Float, Int>{
        if (coefficient == 1){
            if (coordinate + animSpeed > border - radius){
                return Pair(coordinate, -1)
            }
            return Pair(coordinate + animSpeed, 1)
        }
        if (coordinate - animSpeed < radius){
            return Pair(coordinate, 1)
        }
        return Pair(coordinate - animSpeed, -1)
    }
    override fun onDraw(canvas: Canvas?) {
        if (mode == "x") {
            val pair = changeCoordinate(cx, xDirection, measuredWidth.toFloat())
            cx = pair.first
            xDirection = pair.second
        }
        else {
            val pair = changeCoordinate(cy, yDirection, measuredHeight.toFloat())
            cy = pair.first
            yDirection = pair.second
        }
        invalidate()
        super.onDraw(canvas)
        val save = canvas?.save()
        canvas?.drawCircle(cx, cy, radius, paint)
        if (save != null) {
            canvas.restoreToCount(save)
        }
    }
}