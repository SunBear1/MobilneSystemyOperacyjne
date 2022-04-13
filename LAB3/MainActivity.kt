package com.example.lab3_v1

import DataBaseHandler
import Entry
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

var list: MutableList<Entry> = ArrayList()
class MainActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = this
        val db = DataBaseHandler(context)
        val btnSecond: Button = findViewById(R.id.btnSecond)
        val btnInsert: Button = findViewById(R.id.btnInsert)
        val btnRead: Button = findViewById(R.id.btnRead)
        val btnSave1: Button = findViewById(R.id.btnStorage)
        val textInput: EditText = findViewById(R.id.editText)
        val tvResult: TextView = findViewById(R.id.tvResult)


        btnInsert.setOnClickListener {
            if (textInput.text.toString().isNotEmpty()) {

                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                val formatted = current.format(formatter)

                db.insertData(textInput.text.toString(), formatted)
                textInput.text.clear()
            }
            else {
                Toast.makeText(context, "Wypełnij pole tekstowe!", Toast.LENGTH_SHORT).show()
            }
        }
        btnRead.setOnClickListener {
            val data = db.readData()
            tvResult.text = ""
            for (i in 0 until data.size) {
                list.add(Entry(data[i].text,data[i].date))
                tvResult.append(
                    data[i].text + " " + data[i].date + "\n"
                )
            }
        }
        btnSave1.setOnClickListener {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/txt");
            intent.putExtra(Intent.EXTRA_TITLE, "file.txt");
            intent.putExtra(
                DocumentsContract.EXTRA_INITIAL_URI,
                Environment.DIRECTORY_DOCUMENTS);
            startActivityForResult(intent, 111);

        }
        btnSecond.setOnClickListener {
            val secondIntent = Intent(this, SecondActivity::class.java)
            startActivity(secondIntent)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            var uri: Uri? = null
            if (resultData != null) {
                uri = resultData.data
                val pfd = contentResolver.openFileDescriptor(uri!!, "w")
                val fileOutputStream = FileOutputStream(pfd!!.fileDescriptor)
                for (i in 0 until list.size)
                {
                    val line = list[i].text + " " + list[i].date + "\n"
                    fileOutputStream.write(line.toByteArray())
                }
            }
        }
    }


}