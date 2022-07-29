package com.example.videoplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.exoplayer2.util.FlagSet
import com.google.android.exoplayer2.util.Log
import com.google.common.collect.ImmutableList
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerControlView


class MainActivity : AppCompatActivity(), Player.Listener {
    private lateinit var player: ExoPlayer
    private lateinit var playerView: StyledPlayerView
    private lateinit var progressBar: ProgressBar
    private lateinit var titleTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar)
        titleTv = findViewById(R.id.title)

        setupPlayer()
        addMP4Files()

        if (savedInstanceState != null) {
            if (savedInstanceState.getInt("mediaItem") != 0) {
                val restoredMediaItem = savedInstanceState.getInt("mediaItem")
                val seekTime = savedInstanceState.getLong("SeekTime")
                player.seekTo(restoredMediaItem, seekTime)
                player.play()
            }
        }
    }



    private fun addMP4Files() {
        val mediaItem = MediaItem.fromUri(getString(R.string.media_url_mp4))
        val newItems: List<MediaItem> = ImmutableList.of(mediaItem)

        player.addMediaItems(newItems)
        player.prepare()
    }

    private fun setupPlayer() {
        player = ExoPlayer.Builder(this).build()
        playerView = findViewById(R.id.video_view)
        playerView.player = player
        player.addListener(this)
        TimeCodes()

    }

    private fun TimeCodes(){
        val button : Button = findViewById(R.id.button)
        button.setOnClickListener{
            player.seekTo(10000)
        }

        val button2 : Button = findViewById(R.id.button2)
        button2.setOnClickListener{
            player.seekTo(60000)
        }

        val button3 : Button = findViewById(R.id.button3)
        button3.setOnClickListener{
            player.seekTo(180000)
        }

        val button4 : Button = findViewById(R.id.button4)
        button4.setOnClickListener{
            player.seekTo(300000)
        }

        val button5 : Button = findViewById(R.id.button5)
        button5.setOnClickListener{
            player.seekTo(480000)
        }

        val button6 : Button = findViewById(R.id.button6)
        button6.setOnClickListener{
            player.seekTo(600000)
        }

    }

    override fun onStop() {
        super.onStop()
        player.release()
    }

    override fun onResume() {
        super.onResume()
        println(player.currentPosition)
        setupPlayer()
        addMP4Files()

    }



    override fun onPlaybackStateChanged(state: Int) {
        when (state) {
            Player.STATE_BUFFERING -> {
                progressBar.visibility = View.VISIBLE
            }
            Player.STATE_READY -> {
                progressBar.visibility = View.INVISIBLE
            }
        }
    }


    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {

        titleTv.text = mediaMetadata.title ?: mediaMetadata.displayTitle ?: "Заголовок не найден"

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState: " + player.currentPosition)

        outState.putLong("SeekTime", player.currentPosition)

        outState.putInt("mediaItem", player.currentMediaItemIndex)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onSaveInstanceState: " + player.currentPosition)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}