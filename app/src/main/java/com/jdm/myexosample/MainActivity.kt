package com.jdm.myexosample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.ExoPlayer
import com.jdm.myexosample.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_main
    private lateinit var player: ExoPlayer
    override fun initView() {
        player = ExoPlayer.Builder(this).build()
    }

    override fun subscribe() {
    }

}