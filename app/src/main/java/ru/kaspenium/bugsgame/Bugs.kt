package ru.kaspenium.bugsgame

import android.graphics.Bitmap
import android.graphics.Canvas

class Bugs(
    var x: Float,
    var y: Float,
    private var speedX: Float,
    private var speedY: Float,
    val bitmap: Bitmap,
    private val screenHeight: Int,
    private val screenWidth: Int
) :
    Thread() {
    private var isAlive = true

    fun kill(){
        isAlive = false
    }

    fun move(screenWidth: Int, screenHeight: Int) {
        if (x <= 0 || x >= screenWidth - bitmap.width) {
            speedX = -speedX
        }
        if (y <= 0 || y >= screenHeight - bitmap.height) {
            speedY = -speedY
        }
        x += speedX
        y += speedY
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, x, y, null)
    }

    override fun run() {

        while (isAlive) {
            move(screenWidth, screenHeight)
            try {
                sleep(15)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}