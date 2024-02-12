package ru.kaspenium.bugsgame

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class GameOverActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        val mes = intent.getStringExtra("message")
        val textView = findViewById<TextView>(R.id.textView)
        textView.text = mes
        mediaPlayer = MediaPlayer.create(this, R.raw.game_over_sound)

        mediaPlayer?.let {
            it.isLooping = true
            it.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}