package com.example.szachy_mobilne_2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class ChessView(context: Context?, attrs: AttributeSet?) : View(context,attrs) {
    val bok = 130f
    val odleglosc_od_lewej = 25f
    val odleglosc_od_gory = 500f
    val light_color = Paint()
    val dark_color = Paint()
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        light_color.color = Color.LTGRAY
        dark_color.color = Color.CYAN
        val colors = arrayOf<Paint>(light_color,dark_color)
        for(j in 0..7) {
            for (i in 0..7) {
                canvas.drawRect(
                    odleglosc_od_lewej + i * bok,
                    odleglosc_od_gory + j * bok ,
                    odleglosc_od_lewej + (i + 1) * bok,
                    odleglosc_od_gory + (j + 1)*bok,
                    colors[(i + j) % 2]
                )

            }
        }
    }

}