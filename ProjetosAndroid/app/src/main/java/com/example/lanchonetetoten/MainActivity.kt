package com.example.lanchonetetoten

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var btnstartrnewactivity : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_On_Off = findViewById<View>(R.id.btn_On_Off) as ToggleButton

        val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.coffeshop)
        mediaPlayer.isLooping = false

        if (mediaPlayer.isPlaying != true) {

            mediaPlayer.start()
        }
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        audioManager.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_VIBRATE)

        btn_On_Off.isChecked = true
        btn_On_Off.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                audioManager.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI)
            } else {
                audioManager.adjustVolume(AudioManager.ADJUST_MUTE, AudioManager.FLAG_SHOW_UI)
            }
        }

        btnstartrnewactivity = findViewById(R.id.btnStartNewActivity)

        btnstartrnewactivity.setOnClickListener { val itt = Intent(this, SecondActivity::class.java)
                startActivity(itt)
                mediaPlayer.pause()
        }
    }
}