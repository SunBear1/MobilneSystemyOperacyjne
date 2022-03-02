package com.example.lab_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var textView: TextView = findViewById(R.id.textView1)
        var button1: Button = findViewById(R.id.button1)
        var button2: Button = findViewById(R.id.button2)
        var progressBar: ProgressBar = findViewById(R.id.progressBar)
        var start = false
        var progress = 0
        var i= 0
        val handler = Handler()

        button1.setOnClickListener { textView.text = "Breaking the RSA keys..."
            i = progressBar.progress
            start = true
            Thread(Runnable {
                // this loop will run until the value of i becomes 99
                while (i < 100) {
                    if (start == true)
                    {
                        i += 1
                        progressBar.progress = i
                    }
                    try {
                        Thread.sleep(800)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }

                // setting the visibility of the progressbar to invisible
                // or you can use View.GONE instead of invisible
                // View.GONE will remove the progressbar
                textView.text = "Calculations completed"

            }).start()


        }
        button2.setOnClickListener {
            start = false
            textView.text = "Calculations stopped"}

    }

}