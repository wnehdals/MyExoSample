package com.jdm.myexosample.ui.play

import android.app.PictureInPictureParams
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.util.Rational
import android.view.Display
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.jdm.myexosample.base.BaseDialog
import com.jdm.myexosample.const.FIRST_VIEW_POSITION
import com.jdm.myexosample.const.PHONE
import com.jdm.myexosample.const.SEEK_BACKWARD_MS
import com.jdm.myexosample.const.SEEK_FORWARD_MS
import com.jdm.myexosample.const.SELECTED_VIDEO
import com.jdm.myexosample.const.VIDEO_LIST
import com.jdm.myexosample.databinding.ActivityPlayBinding
import com.jdm.myexosample.response.Video
import com.jdm.myexosample.state.PlayState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.exo_player_control_view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
@AndroidEntryPoint
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
        viewModel.getVideoEntity()
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
        viewModel.playState.observe(this) {
            when (it) {
                is PlayState.Loading -> {
                    binding.playProgressbar.visibility = View.VISIBLE
                }
                is PlayState.Pause -> {

                }
                is PlayState.Playing -> {
                }
                is PlayState.Ready -> {
                    binding.playProgressbar.visibility = View.GONE
                    player?.play()
                }
                is PlayState.Finish -> {
                }
            }
        }
        viewModel.currentPosition.observe(this) {
            if (it > FIRST_VIEW_POSITION) {
                Toast.makeText(this, getString(R.string.current_position_guide), Toast.LENGTH_SHORT).show()
            }
            readyToPlay()
        }

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
        viewModel.getVideoEntity()
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
        player?.seekTo(viewModel.currentPosition.value!!)
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
        viewModel.getVideoEntity()
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

    override fun onStart() {
        super.onStart()

    }
    override fun onStop() {
        super.onStop()

        binding.playerView.player?.let {
            viewModel.currentPosition.value = it.currentPosition
            it.pause()
            viewModel.saveVideoPosition(it.currentPosition)
        }
        if (viewModel.isPIP) {
            finishAndRemoveTask()
        }

    }

    override fun onBackPressed() {
        if (isPossibleVersion()) {
            startPIP()
        } else {
            super.onBackPressed()
        }

    }
    fun isPossibleVersion(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        viewModel.isPIP = isInPictureInPictureMode
        _binding = DataBindingUtil.setContentView(this, layoutId)
        if (viewModel.isPIP) {
            binding.playProgressbar.visibility = View.GONE
        } else {
            binding.playProgressbar.visibility = View.GONE
        }

    }

    override fun onDestroy() {
        player?.release()
        player = null
        super.onDestroy()
    }

}