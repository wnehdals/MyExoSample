package com.jdm.myexosample.ui.play

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.view.Display
import android.view.Surface
import androidx.activity.viewModels
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.jdm.myexosample.PlayBackStateListener
import com.jdm.myexosample.R
import com.jdm.myexosample.base.BaseActivity
import com.jdm.myexosample.const.SEEK_BACKWARD_MS
import com.jdm.myexosample.const.SEEK_FORWARD_MS
import com.jdm.myexosample.const.SELECTED_VIDEO
import com.jdm.myexosample.const.VIDEO_LIST
import com.jdm.myexosample.databinding.ActivityPlayBinding
import com.jdm.myexosample.response.Video
import com.jdm.myexosample.state.PlayState
import com.jdm.myexosample.ui.main.VideoListAdapter
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.exo_player_control_view.*

class PlayActivity : BaseActivity<ActivityPlayBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_play
    private val viewModel: PlayViewModel by viewModels()
    private lateinit var videos: ArrayList<Video>
    private var player: ExoPlayer? = null
    private var videoAdapter = PlayListAdapter()
    private lateinit var playBackStateListener: PlayBackStateListener
    override fun initView() {
        if (getDisplayInstance()?.rotation == Surface.ROTATION_0) {
            viewModel.isFull.value = false
        }
        setIntentData()
        setVideoList()
        setMediaItem()
        Log.e("sdfsdf","oncreate")

        player = ExoPlayer.Builder(this)
            .setSeekForwardIncrementMs(SEEK_FORWARD_MS)
            .setSeekBackIncrementMs(SEEK_BACKWARD_MS)
            .build()
        with(binding) {
            videoAdapter.apply {
                onClick = this@PlayActivity::onClickVideoItem
            }
            player_recyclerview.adapter = videoAdapter
            playerView.player = player
        }


        playBackStateListener = PlayBackStateListener(viewModel)
        player?.addListener(playBackStateListener)
        readyToPlay()
    }

    override fun initEvent() {
        with(binding) {
            exo_fullscreen.setOnClickListener {
                if (viewModel.isFull.value!!) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                } else {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
            }
        }
    }

    override fun subscribe() {
        viewModel.playState.observe(this, {
            when (it) {
                is PlayState.Loading -> {

                }
                is PlayState.Pause -> {

                }
                is PlayState.Playing -> {

                }
                is PlayState.Ready -> {
                    player?.play()
                }
                is PlayState.Finish -> {

                }
            }
        })

    }
    private fun getDisplayInstance(): Display? {
        var displayInstance: Display? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            displayInstance = display
        } else {
            @Suppress("DEPRECATION")
            displayInstance = windowManager.defaultDisplay
        }
        return displayInstance
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewModel.isFull.value = true
        } else {
            viewModel.isFull.value = false
        }
    }
    private fun setIntentData() {
        videos = intent.getParcelableArrayListExtra<Video>(VIDEO_LIST) ?: ArrayList()
        videos.add(0, Video())
        viewModel.videoList.addAll(videos)
        viewModel.currentIdx = intent.getIntExtra(SELECTED_VIDEO, 0) + 1
    }
    private fun setVideoList() {
        viewModel.videoList[0] = viewModel.videoList[viewModel.currentIdx].copy(id = -1)
        videoAdapter.submitList(viewModel.videoList)
    }
    private fun setMediaItem() {
        viewModel.mediaItem = createMediaItem(viewModel.videoList[viewModel.currentIdx])
    }
    private fun createMediaItem(video: Video): MediaItem {
        return MediaItem.Builder()
            .setUri(video.url)
            .setMediaId("${video.id}")
            .build()
    }
    private fun readyToPlay() {
        player?.setMediaItem(viewModel.mediaItem!!)
        player?.prepare()
    }
    private fun deleteMediaItem() {
        player?.pause()
        player?.removeMediaItem(0)
    }
    private fun changeVideoInfoText() {
        viewModel.videoList[0] = viewModel.videoList[viewModel.currentIdx].copy(id = -1)
        videoAdapter.notifyItemChanged(0)
    }
    private fun onClickVideoItem(position: Int) {
        viewModel.currentIdx = position
        changeVideoInfoText()
        deleteMediaItem()
        setMediaItem()
        readyToPlay()
    }
    override fun onDestroy() {
        player?.release()
        player = null
        super.onDestroy()
    }
}