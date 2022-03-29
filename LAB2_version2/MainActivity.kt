package com.example.lab_2_v2

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var button: Button = findViewById(R.id.button)

        button.setOnClickListener {
            val drawIntent = Intent(this, SecondActivity::class.java)
            startActivityForResult(drawIntent, 420);
        }
    }


    override fun onBackPressed() {
        val adb: AlertDialog.Builder = AlertDialog.Builder(this)
        adb.setTitle("Do you want to exit?")
        adb.setPositiveButton("Yeah",
            DialogInterface.OnClickListener { dialog, which -> super@MainActivity.onBackPressed() })
        adb.setNegativeButton("Nah",
            DialogInterface.OnClickListener { dialog, which -> })
        adb.create().show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 420) {
            if (resultCode == RESULT_OK) {
                val bitmap = data!!.getParcelableExtra("drawing") as Bitmap?
                var imgView: ImageView = findViewById(R.id.imageView)
                imgView.setImageBitmap(bitmap)
            }
        }
    }
}