package com.example.finalyearproject.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import com.example.finalyearproject.R
import kotlinx.android.synthetic.main.activity_guide.*

class GuideActivity : AppCompatActivity() {
    private var video: VideoView? = null
    private var mediaController: MediaController? = null
    private var uri: Uri? = null
    private var isContinuously = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        video = findViewById(R.id.video)
        mediaController = MediaController(this)
        mediaController!!.setAnchorView(video)
        val uriPath = "android.resource://" + packageName + "/" + R.raw.guide
        uri = Uri.parse(uriPath)
        video!!.setOnCompletionListener {
            if (isContinuously) {
                video!!.start()
            }
        }
        backBtn.setOnClickListener {
            val intent = Intent(this, FormHelperActivity::class.java)
            startActivity(intent)
        }
        stopBtn!!.setOnClickListener{
            video!!.stopPlayback()
        }

        onceBtn!!.setOnClickListener {
            isContinuously = false
            video!!.setMediaController(mediaController)
            video!!.setVideoURI(uri)
            video!!.requestFocus()
            video!!.start()
        }
        continueBtn!!.setOnClickListener {
            isContinuously = true
            video!!.setMediaController(mediaController)
            video!!.setVideoURI(uri)
            video!!.requestFocus()
            video!!.start()
        }


    }
}
