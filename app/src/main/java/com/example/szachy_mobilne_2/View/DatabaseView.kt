package com.example.szachy_mobilne_2.View

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DatabaseView(context: Context?, attrs: AttributeSet?) : View(context,attrs) {
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val testPaint = Paint();
        testPaint.color = Color.RED;
        canvas.drawRect(0F, 0F,200F,300F, testPaint)
    }

}