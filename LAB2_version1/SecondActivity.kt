package com.example.lab_2

import android.R.attr.data
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        var textView: TextView = findViewById(R.id.textView)
        val message = intent.getStringExtra("TextMessage")
        textView.text = message
    }
    override fun onBackPressed() {
        val resultIntent = Intent()
        resultIntent.putExtra("Result", "2nd Activity got the message")
        setResult(RESULT_OK, resultIntent)
        finish()
    }



}