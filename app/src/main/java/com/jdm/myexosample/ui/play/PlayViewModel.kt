package com.jdm.myexosample.ui.play

import android.media.browse.MediaBrowser
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.MediaItem
import com.jdm.myexosample.base.BaseViewModel
import com.jdm.myexosample.response.Video
import com.jdm.myexosample.state.BaseState
import com.jdm.myexosample.state.PlayState

class PlayViewModel: BaseViewModel() {
    val videoList: MutableList<Video> = mutableListOf()


    var mediaItem: MediaItem? = null
    var portraitWidth = 0
    var portraitHeight = 0
    val playState = MutableLiveData<PlayState>(PlayState.Uninitialized)
    var currentIdx = 0
    var currentPosition = 0L
    val isFull = MutableLiveData<Boolean>(false)
    var isPortrait = true
    var isPhone = false

}