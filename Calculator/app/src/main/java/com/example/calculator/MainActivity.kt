package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.Button0

class MainActivity : AppCompatActivity() {
    companion object {
        const val EXPRESSION = "EXPRESSION"
        const val LABEL_TEXT = "LABEL_TEXT"
        const val  LAST_OPERATION = "LAST_OPERATION"
    }

    lateinit var label : TextView

    private var expression : Double = 0.0
    private var isResultNow = false
    private var isCommaNow = false
    var lastOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        label = findViewById(R.id.textView)
    }
    fun onNumberClickEvent(view: View?){
        if (view is Button){
            val text : String
            val labelText = label.text?.toString()
            if (labelText?.compareTo("0", true) == 0 || isResultNow){
                text = view.text.toString()
                isResultNow = false
            }
            else {
                text = buildString {
                    append(label.text)
                    append(view.text)
                }
            }
            label.text = text
        }
    }

    fun onOperationClickEvent(view: View?){
        if (view is Button && label.text != null){
            isCommaNow = false
            val labelText = label.text.toString()
            expression = labelText.toDouble()
            lastOperation = view.text.toString()
            label.text = "0"
        }
    }

    fun onResultClickEvent(view: View?){
        if (view is Button && label.text != null){
            isCommaNow = false
            val labelText = label.text.toString()
            val localExpression = labelText.toDouble()
            println(lastOperation)
            if (lastOperation.compareTo("+") == 0){
                expression += localExpression
            }
            if (lastOperation.compareTo("*") == 0){
                expression *= localExpression
            }
            if (lastOperation.compareTo("-") == 0){
                expression -= localExpression
            }
            if (lastOperation.compareTo("/") == 0){
                expression /= localExpression
            }
            if (lastOperation.compareTo("=") == 0){
                expression = localExpression
            }
            updateLabel()
            lastOperation = "="
            isResultNow = true
        }
    }

    fun onCommaClickEvent(view: View?){
        if (!isCommaNow){
            isCommaNow = true
            if (isResultNow){
                label.text = "0."
                isResultNow = false
            }
            else {
                label.text = buildString {
                    append(label.text?.toString())
                    append(".")
                }
            }
        }
    }

    private fun updateLabel(){
        val intExpression = expression.toInt()
        if (expression - intExpression == 0.0){
            label.text = intExpression.toString()
        }
        else {
            label.text = expression.toString()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putDouble(EXPRESSION, expression)
        outState.putCharSequence(LABEL_TEXT, label.text)
        outState.putString(LAST_OPERATION, lastOperation)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        label.text = savedInstanceState.getCharSequence(LABEL_TEXT)
        expression = savedInstanceState.getDouble(EXPRESSION)
        lastOperation = savedInstanceState.getString(LAST_OPERATION).toString()
    }

}