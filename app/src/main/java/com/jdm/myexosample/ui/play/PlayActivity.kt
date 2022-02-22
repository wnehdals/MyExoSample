package com.jdm.myexosample.ui.play

import android.app.PictureInPictureParams
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.util.Rational
import android.view.Display
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo
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
import com.jdm.myexosample.extension.dp
import com.jdm.myexosample.response.Video
import com.jdm.myexosample.state.PlayState
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.exo_player_control_view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlayActivity : BaseActivity<ActivityPlayBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_play
    private val viewModel: PlayViewModel by viewModels()
    private lateinit var videos: ArrayList<Video>
    private var player: ExoPlayer? = null
    private var videoAdapter = PlayListAdapter()
    private lateinit var playBackStateListener: PlayBackStateListener
    private var pipParamBuilder: PictureInPictureParams.Builder? = null
    override fun initView() {
        Log.e("click", "oncreate")
        setFoldableStateListener()
        viewModel.isPortrait =
            (getDisplayInstance()?.rotation == Surface.ROTATION_0 || getDisplayInstance()?.rotation == Surface.ROTATION_180)
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

    private fun setFoldableStateListener() {
        lifecycleScope.launch(Dispatchers.Main) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                WindowInfoTracker.getOrCreate(this@PlayActivity)
                    .windowLayoutInfo(this@PlayActivity)
                    .collect { newLayoutInfo ->
                        updateStateLog(newLayoutInfo)
                    }
            }
        }
    }

    private fun updateStateLog(layoutInfo: WindowLayoutInfo) {
        for (displayFeature in layoutInfo.displayFeatures) {
            val foldFeature = displayFeature as? FoldingFeature
            if (foldFeature != null) {
                if (foldFeature.orientation == FoldingFeature.Orientation.HORIZONTAL) {

                    if (viewModel.isFull.value == false) {
                        if (foldFeature.state == FoldingFeature.State.HALF_OPENED) {
                            Log.e("foldfeature", "flex mode start")
                            (binding.playerBackgroundConstraintlayout as MotionLayout).transitionToEnd()
                        } else {
                            Log.e("foldfeature", "flex mode out")
                            (binding.playerBackgroundConstraintlayout as MotionLayout).transitionToStart()
                        }
                    }

                } else {
                    Log.e("foldfeature", "hinge is vertical")
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

    private fun bindUI() {
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
                        viewModel.isPortrait = true
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    } else {
                        viewModel.isPortrait = false
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    }
                } else {
                    if (viewModel.isFull.value == true && viewModel.isPortrait == true) {
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
                    } else if (viewModel.isFull.value == false && viewModel.isPortrait == true) {
                        val cl = ConstraintLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        with(binding) {
                            binding.playerConstraintlayout.layoutParams = cl
                            binding.playerRecyclerview.visibility = View.GONE
                        }
                        viewModel.isFull.value = !viewModel.isFull.value!!
                    } else if (viewModel.isFull.value == true && viewModel.isPortrait == false) {
                        _binding = DataBindingUtil.setContentView(this@PlayActivity, layoutId)
                        bindUI()
                        viewModel.isFull.value = !viewModel.isFull.value!!
                    } else {

                        val cl = ConstraintLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        with(binding) {
                            binding.playerConstraintlayout.layoutParams = cl
                            binding.playerRecyclerview.visibility = View.INVISIBLE
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
            viewModel.isPortrait = false
            _binding = DataBindingUtil.setContentView(this, layoutId)
            bindUI()

        } else {
            viewModel.isPortrait = true
            _binding = DataBindingUtil.setContentView(this, layoutId)
            bindUI()
        }
    }

    private fun setIntentData() {
        Log.e("asd","intent")
        videos = intent.getParcelableArrayListExtra<Video>(VIDEO_LIST) ?: ArrayList()
        videos.add(0, Video())
        viewModel.videoList.addAll(videos)
        viewModel.currentIdx = intent.getIntExtra(SELECTED_VIDEO, 0) + 1
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        viewModel.currentIdx = intent?.getIntExtra(SELECTED_VIDEO, 0)!! + 1
        changeVideoInfoText()
        deleteMediaItem()
        setMediaItem()
        readyToPlay()
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
        Log.e("click", "${position}")
        viewModel.currentIdx = position
        changeVideoInfoText()
        deleteMediaItem()
        setMediaItem()
        readyToPlay()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        startPIP()
    }
    override fun onPause() {
        super.onPause()
    }
    fun startPIP() {
        if (isPossibleVersion()) {
            pipParamBuilder = PictureInPictureParams.Builder()
            pipParamBuilder?.let {
                it.setAspectRatio(Rational(16,9))
                enterPictureInPictureMode(it.build())
            }

        }
    }
    override fun onStop() {
        super.onStop()
        Log.e("onstop", "onstop")
        binding.playerView.player?.let {
            viewModel.currentPosition = it.currentPosition
        }
        /*
        if (isPossibleVersion()) {
            finishAndRemoveTask()
        }

         */
    }

    override fun onBackPressed() {
        if (isPossibleVersion()) {
            startPIP()
        } else {
            super.onBackPressed()
        }

    }
    fun isPossibleVersion(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
    }

    override fun onDestroy() {
        Log.e("ondestroy", "onDestroy")
        player?.release()
        player = null
        super.onDestroy()

    }

}