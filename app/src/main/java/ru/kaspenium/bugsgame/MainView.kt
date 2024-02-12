package ru.kaspenium.bugsgame

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.AudioManager
import android.media.SoundPool
import android.view.MotionEvent
import android.view.View
import java.util.Timer
import kotlin.concurrent.timerTask
import kotlin.random.Random


class MainView(context: Context) : View(context) {

    private val bugs: MutableList<Bugs> = ArrayList()
    private var timer: Timer? = null
    private var score: Int = 10
    private val sounds = SoundPool(10, AudioManager.STREAM_MUSIC,0);
    private val hit = sounds.load(context, R.raw.hit_sound, 1);
    private val miss = sounds.load(context, R.raw.miss_sound, 1);

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        postInvalidate()
        val paint = Paint()
        paint.textSize = 50f
        paint.color = Color.WHITE
        canvas.drawText("Score: $score", 50f, 50f, paint)
        for (insect in bugs) {
            insect.draw(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            synchronized(bugs) {
                for (bug in bugs) {
                    if (event.x >= bug.x && event.x < bug.x + bug.bitmap.width
                        && event.y >= bug.y && event.y < bug.y + bug.bitmap.height
                    ) {
                        bug.kill()
                        bugs.remove(bug)
                        sounds.play(hit, 1.0f, 1.0f, 0, 0, 1.5f)
                        score++
                        if (score == 50) gameOver()
                        return true
                    }
                }
            }
            sounds.play(miss, 1.0f, 1.0f, 0, 0, 1.5f)
            score--
            if (score == 0) gameOver()
        }
        return super.onTouchEvent(event)
    }

    private fun spawnRandBug(){
        val x = ((100..680).random().toFloat())
        val y = ((100..1800).random().toFloat())
        val speedX = Random.nextDouble(-15.0, 15.0).toFloat()
        val speedY = Random.nextDouble(-15.0, 15.0).toFloat()
        val bugBitmap: Bitmap = when ((0..2).random()) {
            0 -> BitmapFactory.decodeResource(resources, R.drawable.bug1)
            1 -> BitmapFactory.decodeResource(resources, R.drawable.bug2)
            else -> BitmapFactory.decodeResource(resources, R.drawable.bug3)
        }
        val bugs = Bugs(
            x,
            y,
            speedX,
            speedY,
            bugBitmap,
            2200,
            1080
        )
        this.bugs.add(bugs)
        bugs.start()
    }

    init {
        for (i in 0..5) {
            spawnRandBug()
        }
        timer = Timer()
        timer?.scheduleAtFixedRate(timerTask {
            if (bugs.size < 15) spawnRandBug()
        }, 0, 500)
    }

    private fun gameOver() {
        for (bug in bugs) {
                bug.kill()
        }
        bugs.clear()
        val intent = Intent(context, GameOverActivity::class.java)
        val message = if (score == 0) "YOU LOSE!!!" else "YOU WIN!"
        intent.putExtra("message", message)
        context.startActivity(intent)
        (context as Activity).finish()
    }
}