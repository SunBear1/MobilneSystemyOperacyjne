package com.example.lab_2

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


const val EXTRA_MESSAGE = "com.example.lab_2.MESSAGE"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var editView: EditText = findViewById(R.id.editText)
        var button: Button = findViewById(R.id.button)
        var button2: Button = findViewById(R.id.button4)

        button.setOnClickListener {
            val message = editView.text.toString()
            val secondIntent = Intent(this, SecondActivity::class.java)
            secondIntent.putExtra("TextMessage", message);
            startActivityForResult(secondIntent, 420);
        }

        button2.setOnClickListener {
            val imageCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(imageCaptureIntent, 616)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 420) {
            if (resultCode == RESULT_OK) {
                val returnString = data!!.getStringExtra("Result")
                var textView: TextView = findViewById(R.id.textView2)
                textView.text = returnString
            }
        }
        if (requestCode === 616 && resultCode === RESULT_OK) {
            val bitmap = data!!.getParcelableExtra("data") as Bitmap?
            var imgView: ImageView = findViewById(R.id.imageView)
            imgView.setImageBitmap(bitmap)
        }

    }
}