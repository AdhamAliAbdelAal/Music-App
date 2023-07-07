package com.example.clapapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.fabPause
import kotlinx.android.synthetic.main.activity_main.fabPlay

class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fabPlay.setOnClickListener{
            if(mediaPlayer==null)
                mediaPlayer = MediaPlayer.create(this, R.raw.music)
            mediaPlayer?.start()
        }
        fabPause.setOnClickListener{
            mediaPlayer?.pause()
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer?.start()
    }
}