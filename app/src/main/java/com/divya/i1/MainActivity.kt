package com.divya.i1

import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    lateinit var play : Button
    lateinit var pause : Button
    lateinit var mediaPlayer: MediaPlayer
    var url : String? = null
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var DatabaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseDatabase=FirebaseDatabase.getInstance()
        DatabaseReference=firebaseDatabase.getReference("url")

        DatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                url = snapshot.getValue(String::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Fail to get audio url.", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        play = findViewById(R.id.play)
        pause = findViewById(R.id.pause)
        
        play.setOnClickListener { 
            playaudio(url)
        }

        pause.setOnClickListener {

            if (mediaPlayer.isPlaying){
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.release()

                Toast.makeText(this@MainActivity, "Audio has been paused", Toast.LENGTH_SHORT)
                    .show()
            }
            else{
                Toast.makeText(this@MainActivity, "Audio has not played", Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }

    private fun playaudio(url: String?) {

        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            mediaPlayer.start()

            Toast.makeText(this, "Audio started playing..", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(this, "Error found is $e", Toast.LENGTH_SHORT).show()
        }



    }
}