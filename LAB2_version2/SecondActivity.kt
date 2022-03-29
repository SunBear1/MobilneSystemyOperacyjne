package com.example.lab_2_v2

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import androidx.appcompat.app.AppCompatActivity


class SecondActivity : AppCompatActivity() {

    lateinit var CanvasView: MyCanvasView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CanvasView = MyCanvasView(this)
        CanvasView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
        CanvasView.contentDescription = getString(R.string.canvasContentDescription)
        setContentView(CanvasView)
    }
    override fun onBackPressed() {
        val resultIntent = Intent()
        var resizedDrawing = getResizedBitmap(CanvasView.extraBitmap,350,550)
        resultIntent.putExtra("drawing", resizedDrawing)
        setResult(RESULT_OK, resultIntent)
        finish()
    }


    fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    }
}