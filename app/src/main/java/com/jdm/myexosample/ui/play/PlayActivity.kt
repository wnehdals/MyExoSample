package com.jdm.myexosample.ui.play

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo
import androidx.window.layout.WindowMetricsCalculator
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.jdm.myexosample.PlayBackStateListener
import com.jdm.myexosample.R
import com.jdm.myexosample.base.BaseActivity
import com.jdm.myexosample.const.PHONE
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
class PlayActivity : BaseActivity<ActivityPlayBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_play
    private val viewModel: PlayViewModel by viewModels()
    private lateinit var videos: ArrayList<Video>
    private var player: ExoPlayer? = null
    private var videoAdapter = PlayListAdapter()
    private lateinit var playBackStateListener: PlayBackStateListener
    private val stateLog: StringBuilder = StringBuilder()
    @OptIn(InternalCoroutinesApi::class)
    override fun initView() {
        lifecycleScope.launch(Dispatchers.Main) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                WindowInfoTracker.getOrCreate(this@PlayActivity)
                    .windowLayoutInfo(this@PlayActivity)
                    .collect { newLayoutInfo ->
                        updateStateLog(newLayoutInfo)
                    }
            }
        }
        viewModel.isFull.value =
            !(getDisplayInstance()?.rotation == Surface.ROTATION_0 || getDisplayInstance()?.rotation == Surface.ROTATION_180)
        checkDeviceType()
        setIntentData()
        setVideoList()
        setMediaItem()
        buildPlayer()
        bindUI()



        playBackStateListener = PlayBackStateListener(viewModel)
        player?.addListener(playBackStateListener)
        readyToPlay()
    }
    private fun setDisplayRatio() {
        val windowMetrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
        val currentBounds = windowMetrics.bounds
        viewModel.portraitWidth = currentBounds.width()
        viewModel.portraitHeight = currentBounds.width() * 9 / 16
        Log.e("sdfsdf","${viewModel.portraitWidth} / ${viewModel.portraitHeight}")
    }
    private fun updateStateLog(layoutInfo: WindowLayoutInfo) {
        for (displayFeature in layoutInfo.displayFeatures) {
            val foldFeature = displayFeature as? FoldingFeature
            if (foldFeature != null) {
                if (foldFeature.orientation == FoldingFeature.Orientation.HORIZONTAL) {
                    Log.e("foldfeature","hinge is horizontal")

                    if (viewModel.isFull.value == false) {
                        if(foldFeature.state == FoldingFeature.State.HALF_OPENED) {
                            Log.e("foldfeature","flex mode start")
                            var cl = binding.playerConstraintlayout.layoutParams as ConstraintLayout.LayoutParams
                            cl.bottomToTop = binding.playerRecyclerview.id
                            cl.dimensionRatio = null
                            binding.playerConstraintlayout.layoutParams = cl
                        } else {
                            Log.e("foldfeature","flex mode out")
                            var cl = binding.playerConstraintlayout.layoutParams as ConstraintLayout.LayoutParams
                            cl.bottomToTop = ConstraintLayout.LayoutParams.UNSET
                            cl.dimensionRatio = "H,16:9"
                            binding.playerConstraintlayout.layoutParams = cl
                        }

                    }

                } else {
                    Log.e("foldfeature","hinge is vertical")
                }

            }
        }
    }
    private fun checkDeviceType() {
        viewModel.isPhone = getString(R.string.screen_size) == PHONE
    }
    private fun buildPlayer() {
        player = ExoPlayer.Builder(this)
            .setSeekForwardIncrementMs(SEEK_FORWARD_MS)
            .setSeekBackIncrementMs(SEEK_BACKWARD_MS)
            .build()

    }
    private fun bindUI () {
        with(binding) {
            videoAdapter.apply {
                onClick = this@PlayActivity::onClickVideoItem
            }
            player_recyclerview.adapter = videoAdapter
            playerView.player = player
        }
        initEvent()

    }

    override fun initEvent() {
        with(binding) {
            exo_fullscreen.setOnClickListener {
                if (viewModel.isPhone) {
                    if (viewModel.isFull.value == true) {
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    } else {
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    }
                } else {
                    if (viewModel.isFull.value == true) {
                        val cl = ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                        )
                        with(binding) {
                            cl.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                            cl.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                            cl.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                            cl.dimensionRatio = "H,16:9"
                            playerConstraintlayout.layoutParams = cl
                            binding.playerRecyclerview.visibility = View.VISIBLE
                        }
                        viewModel.isFull.value = !viewModel.isFull.value!!
                    } else {
                        val cl = ConstraintLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        with(binding) {
                            binding.playerConstraintlayout.layoutParams = cl
                            binding.playerRecyclerview.visibility = View.GONE
                        }
                        viewModel.isFull.value = !viewModel.isFull.value!!
                    }
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
            Log.e("onconfig","land")
            viewModel.isFull.value = true
            _binding = DataBindingUtil.setContentView(this, layoutId)
            bindUI()

        } else {
            Log.e("onconfig","port")
            viewModel.isFull.value = false
            _binding = DataBindingUtil.setContentView(this, layoutId)
            bindUI()
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


    override fun onStop() {
        super.onStop()
        binding.playerView.player?.let {
            viewModel.currentPosition = it.currentPosition
        }
    }
    override fun onDestroy() {
        player?.release()
        player = null
        super.onDestroy()
    }
}