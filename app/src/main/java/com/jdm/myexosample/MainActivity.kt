package com.jdm.myexosample

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.jdm.myexosample.databinding.ActivityMainBinding
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.PlayerMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_main
    private lateinit var player: ExoPlayer
    private val viewModel: MainViewModel by viewModels()
    override fun initView() {
        player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player
        viewModel.getVideoList()
        /*
        var mediaItems = mutableListOf<MediaItem>()
        //mediaItems.add(MediaItem.fromUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
        //mediaItems.add(MediaItem.fromUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"))
        //player.setMediaItems(mediaItems)

        var licenssRequestHeader = HashMap<String, String>()
        licenssRequestHeader.put("pallycon-customdata-v2", "ew0KICAgICJkcm1fdHlwZSI6IldpZGV2aW5lIiwNCiAgICAic2l0ZV9pZCI6IkxYUUQiLA0KICAgICJ1c2VyX2lkIjoicGFuODg5OSIsDQogICAgImNpZCI6Imdyb3ctZGFzaCIsIA0KICAgICJwb2xpY3kiOiIiLA0KICAgICJ0aW1lc3RhbXAiOiIiLA0KICAgICJyZXNwb25zZV9mb3JtYXQiOiAib3JpZ2luYWwiLA0KICAgICJrZXlfcm90YXRpb24iOiBmYWxzZQ0KfQ==")

        var mediaItem2 = MediaItem.Builder()
            .setUri("https://vod.grow.co.kr/assets/a5705b45-b9e0-46fc-bd21-8caf9d2f1e93/DashIso/bcthxvxajl.mpd")
            .setDrmConfiguration(
                MediaItem.DrmConfiguration.Builder(C.WIDEVINE_UUID)
                    .setLicenseUri("https://license-global.pallycon.com/ri/licenseManager.do")
                    .setMultiSession(true)
                    .setLicenseRequestHeaders(licenssRequestHeader)
                    .build()
            )
            .setMediaId("mediaId2")
            .setTag("metadata2")
            .build()
        player.addMediaItem(mediaItem2)
        var listener = PlayBackStateListener()
        player.addListener(listener)
        player.prepare()
        player.play()

         */

    }

    override fun subscribe() {
    }

}