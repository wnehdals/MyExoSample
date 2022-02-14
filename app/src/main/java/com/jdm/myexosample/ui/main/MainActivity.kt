package com.jdm.myexosample.ui.main

import android.content.Intent
import android.util.Log
import com.jdm.myexosample.databinding.ActivityMainBinding
import androidx.activity.viewModels
import com.jdm.myexosample.base.BaseActivity
import com.jdm.myexosample.R
import com.jdm.myexosample.const.SELECTED_VIDEO
import com.jdm.myexosample.const.VIDEO_LIST
import com.jdm.myexosample.response.Video
import com.jdm.myexosample.state.BaseState
import com.jdm.myexosample.ui.play.PlayActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_main
    private val videoAdapter = VideoListAdapter()
    private val viewModel: MainViewModel by viewModels()
    override fun initView() {
        viewModel.getVideoList()

        videoAdapter.apply {
            onClick = this@MainActivity::onClickVideoItem
        }
        binding.mainRecyclerview.adapter = videoAdapter
        Log.e("screensize",getString(R.string.screen_size))

    }

    override fun initEvent() {

    }

    override fun subscribe() {
        viewModel.videoListState.observe(this, {
            when (it) {
                is BaseState.Loading -> {
                    showProgressDialog()
                }
                is BaseState.Success<*> -> {
                    dismissProgressDialog()
                    videoAdapter.submitList(it.successResp as MutableList<Video>)
                }
                is BaseState.Fail -> {
                    dismissProgressDialog()
                    showSimpleDialog(context = this, message = it.failResp.message)
                }
            }
        })
    }

    private fun onClickVideoItem(position: Int) {
        Intent(this, PlayActivity::class.java)
            .putParcelableArrayListExtra(VIDEO_LIST, viewModel.videoList)
            .putExtra(SELECTED_VIDEO, position)
            .run { startActivity(this) }
    }
}