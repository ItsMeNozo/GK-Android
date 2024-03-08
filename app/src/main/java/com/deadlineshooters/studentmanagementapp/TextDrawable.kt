package com.deadlineshooters.studentmanagementapp

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

class TextDrawable(text : String, inputTextSize: Float) : Drawable() {
    private val paint = Paint().apply {
        color = Color.GRAY
        textAlign = Paint.Align.CENTER
        textSize = inputTextSize
    }
    private val text = text

    override fun draw(canvas: Canvas) {
        canvas.drawRect(bounds, paint)
        paint.color = Color.BLACK
        val xPos = bounds.width() / 2
        val yPos = (bounds.height() / 2 - (paint.descent() + paint.ascent()) / 2).toInt()
        canvas.drawText(text, xPos.toFloat(), yPos.toFloat(), paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}
