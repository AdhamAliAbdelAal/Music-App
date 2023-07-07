package com.example.clapapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.fabPause
import kotlinx.android.synthetic.main.activity_main.fabPlay
import kotlinx.android.synthetic.main.activity_main.fabStop
import kotlinx.android.synthetic.main.activity_main.sbClapping
import kotlinx.android.synthetic.main.activity_main.tvDue
import kotlinx.android.synthetic.main.activity_main.tvPlayed


class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer?=null
    lateinit var runnable: Runnable
    private var handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fabPlay.setOnClickListener{
            if(mediaPlayer==null){
                // at the first time mediaPlayer is null
                // or when the user clicks on the stop button and need to play the music again
                mediaPlayer = MediaPlayer.create(this, R.raw.music)
                initializeSeekBar()
            }
            mediaPlayer?.start()
        }
        fabPause.setOnClickListener{
            mediaPlayer?.pause()
        }
        fabStop.setOnClickListener {
            // .stop() is used to stop the music
            mediaPlayer?.stop()
            // .reset() is used to reset the music which means it will start from the beginning
            mediaPlayer?.reset()
            // .release() is used to release the music that means it releases the memory
            mediaPlayer?.release()
            // mediaPlayer is set to null for safety
            mediaPlayer = null
            // the below code is used to stop the runnable
            handler.removeCallbacks(runnable)
            // the below code is used to set the progress of the seekbar to 0
            sbClapping.progress = 0

            tvPlayed.setText(calculateTime(0))
        }
    }

    private fun initializeSeekBar() {
        sbClapping.setOnSeekBarChangeListener(
            object:SeekBar.OnSeekBarChangeListener{
                // the below function is used to change the progress of the seekbar
                // the first parameter is the seekbar itself
                // the second parameter is the progress of the seekbar it ranges from 0 to 100
                // the third parameter is a boolean value which is true if the progress is changed by the user
                // the progress may be changed by the user or by the program itself
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if(fromUser){
                        mediaPlayer?.seekTo(progress)
                    }
                }
                // the below function is used to notify the user when the user starts to touch the seekbar
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                // the below function is used to notify the user when the user stops touching the seekbar
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            }
        )

        // set the maximum value of the seekbar
        sbClapping.max = mediaPlayer!!.duration

        // the below code is used to set the total time of the music
        val totalTime = mediaPlayer!!.duration
        tvDue.setText(calculateTime(totalTime))
        tvPlayed.setText(calculateTime(0))

        // the below code is used to update the progress of the seekbar
        runnable = Runnable {
            // set the current time of the music
            val playedTime = mediaPlayer!!.currentPosition
            tvPlayed.setText(calculateTime(playedTime))
            // set the progress of the seekbar to the current position of the music
            sbClapping.progress = mediaPlayer!!.currentPosition
            // the below code is used to repeat the above code after every 1000 milliseconds
            handler.postDelayed(runnable, 500)
        }
        // the below code is used to start the above code
        handler.postDelayed(runnable, 500)
        // How handler runs at the first time
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer?.start()
    }

    private fun calculateTime(time: Int): String
    {

        val timeInSeconds = time / 1000
        val totalMinutes = timeInSeconds / 60
        val minutes = convertToTwoDigits(totalMinutes % 60)
        val hours= convertToTwoDigits(totalMinutes / 60)
        val seconds = convertToTwoDigits(timeInSeconds % 60)
        return "$hours:$minutes:$seconds"
    }
    private fun convertToTwoDigits(number: Int): String {
        return if (number < 10) "0$number" else number.toString()
    }
}