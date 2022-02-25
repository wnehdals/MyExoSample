package com.jdm.myexosample.ui.play

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.MediaItem
import com.jdm.myexosample.base.BaseViewModel
import com.jdm.myexosample.repository.Repository
import com.jdm.myexosample.response.Video
import com.jdm.myexosample.state.PlayState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlayViewModel @Inject constructor(private val repository: Repository): BaseViewModel() {
    val currentPosition = MutableLiveData<Long>(0)
    val videoList: MutableList<Video> = mutableListOf()
    var mediaItem: MediaItem? = null
    val playState = MutableLiveData<PlayState>(PlayState.Uninitialized)
    var currentIdx = 0
    val isFull = MutableLiveData<Boolean>(false)
    var isPortrait = true
    var isPhone = false
    var isPIP = false

    fun saveVideoPosition(position: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var video = videoList[currentIdx]
                repository.insertVideoEntity(video.toVideoEntity(position))
            }
        }
    }
    fun getVideoEntity() {
        viewModelScope.launch {
                var video = videoList[currentIdx]
                repository.getVideoEntity(video.id)
                    .collect {
                        currentPosition.value = it.position
                    }


        }
    }

}