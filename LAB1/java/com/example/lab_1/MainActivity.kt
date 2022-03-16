package com.example.lab_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var textView: TextView = findViewById(R.id.textView1)
        var button1: Button = findViewById(R.id.button1)
        var button2: Button = findViewById(R.id.button2)
        var progressBar: ProgressBar = findViewById(R.id.progressBar)
        var start = false
        var threadCreated = false
        var finished = false

        button1.setOnClickListener {
            start = true
            if (!finished) {
                textView.text = "Breaking the RSA keys..."
            }
            var i = progressBar.progress
            if (!threadCreated) {
                threadCreated = true
                Thread(Runnable {
                    while (i < 100) {
                        if (start) {
                            i += 1
                            progressBar.progress = i
                        }
                        try {
                            Thread.sleep(600)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }
                    textView.text = "Calculations completed"
                    finished = true
                }).start()
            }
        }
        button2.setOnClickListener {
            start = false
            if (!finished){
                textView.text = "Calculations stopped"
            }
        }
    }
}
