package com.example.lab3_v1

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import java.io.*


class SecondActivity : AppCompatActivity() {

    private var myAdapter: ArrayAdapter<String>? = null
    private var lastRead: String = ""

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        if (ContextCompat.checkSelfPermission(
                this@SecondActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !==
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@SecondActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@SecondActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@SecondActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
                )
            }
        }


        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        lastRead = sharedPref.getString(getString(R.string.saved_last_read_key), "none").toString()

        val btnSave: Button = findViewById(R.id.btnSaveAsNew)
        val list: ListView = findViewById(R.id.listView)
        val path = "/storage/emulated/0/Download/"
        var txtView: TextView = findViewById(R.id.textView)
        var lastReadText: TextView = findViewById(R.id.textView2)
        var fileDir: File = File(path)
        var files = fileDir.list()
        myAdapter = ArrayAdapter(this, R.layout.row, files)
        list.adapter = myAdapter
        lastReadText.setText(lastRead)
        btnSave.setOnClickListener {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "application/txt"
            intent.putExtra(Intent.EXTRA_TITLE, "NEWfile.txt")
            intent.putExtra(
                DocumentsContract.EXTRA_INITIAL_URI,
                Environment.DIRECTORY_DOCUMENTS
            )
            startActivityForResult(intent, 111)

        }
        list.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                val listItem = list.getItemAtPosition(position)
                //lastRead = view.id.toString()
                lastRead = path + "/" + listItem.toString()
                val sharedPref =
                    this.getPreferences(Context.MODE_PRIVATE) ?: return@OnItemClickListener
                with(sharedPref.edit()) {
                    putString(getString(R.string.saved_last_read_key), lastRead)
                    apply()
                }
                val fl: File = File(path + "/" + listItem.toString())
                val fin = FileInputStream(fl)
                val ret = convertStreamToString(fin)
                txtView.text = ret
                lastReadText.setText(lastRead)
                fin.close()
            }
    }

    @Throws(Exception::class)
    fun convertStreamToString(`is`: InputStream?): String? {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String? = null
        while (reader.readLine().also { line = it } != null) {
            sb.append(line).append("\n")
        }
        reader.close()
        return sb.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            var uri: Uri? = null
            if (resultData != null) {
                uri = resultData.data
                val pfd = contentResolver.openFileDescriptor(uri!!, "w")
                val fileOutputStream = FileOutputStream(pfd!!.fileDescriptor)
                var txtView: TextView = findViewById(R.id.textView)
                val line = txtView.text.toString()
                fileOutputStream.write(line.toByteArray())
            }
        }
    }

override fun onRequestPermissionsResult(
    requestCode: Int, permissions: Array<String>,
    grantResults: IntArray
) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    when (requestCode) {
        1 -> {
            if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) {
                if ((ContextCompat.checkSelfPermission(
                        this@SecondActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) ===
                            PackageManager.PERMISSION_GRANTED)
                ) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
            return
        }
    }
}


}