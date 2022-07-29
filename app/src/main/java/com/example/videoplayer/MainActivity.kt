package com.example.videoplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.EventLogger
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
        TimeAcc()
        Cycle()

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

    private fun TimeAcc(){
        val button1 : Button = findViewById(R.id.button_0_5x)
        val button2 : Button = findViewById(R.id.button_1x)
        val button3 : Button = findViewById(R.id.button_1_25x)
        val button4 : Button = findViewById(R.id.button_1_5x)
        val button5 : Button = findViewById(R.id.button_2x)

        button1.setOnClickListener{
            player.setPlaybackSpeed(0.5F)
        }
        button2.setOnClickListener{
            player.setPlaybackSpeed(1.0F)
        }
        button3.setOnClickListener{
            player.setPlaybackSpeed(1.25F)
        }
        button4.setOnClickListener{
            player.setPlaybackSpeed(1.5F)
        }
        button5.setOnClickListener{
            player.setPlaybackSpeed(2.0F)
        }
    }

    private fun Cycle(){
        val table: Array<Array<Int>> = Array(7,{ Array(2,{0}) })
        table[0]= arrayOf(0,10000)
        table[1]= arrayOf(10000,60000)
        table[2]= arrayOf(60000,180000)
        table[3]= arrayOf(180000,300000)
        table[4]= arrayOf(300000,480000)
        table[5]= arrayOf(480000,600000)
        table[6]= arrayOf(600000,654000)
        player.removeListener(this)

        val button: Button = findViewById(R.id.cycle)
        button.setOnClickListener{
            val curTime = player.currentPosition
            for(row in table){
                if(curTime>row[0]&&row[1]>curTime){
                    button.visibility = View.INVISIBLE

                    player.addListener(object : Player.Listener {
                        override fun onEvents(player: Player, events: Player.Events) {
                            super.onEvents(player, events)
                            if(player.currentPosition>=row[1] || player.currentPosition<row[0]){
                                player.seekTo(row[0].toLong())
                            }
                        }

                    })

                }

            }

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