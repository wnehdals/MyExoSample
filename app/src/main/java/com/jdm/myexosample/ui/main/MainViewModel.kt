package com.jdm.myexosample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jdm.myexosample.base.BaseViewModel
import com.jdm.myexosample.repository.Repository
import com.jdm.myexosample.response.Video
import com.jdm.myexosample.state.BaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository): BaseViewModel() {
    private val _videoListState = MutableLiveData<BaseState>(BaseState.Uninitialized)
    val videoListState: LiveData<BaseState> get() = _videoListState

    var videoList = ArrayList<Video>()
    fun getVideoList() {
        viewModelScope.launch(coroutineException) {
            repository.getVideoList(
                onSuccess = {
                    _videoListState.value = BaseState.Success(it)
                    videoList = ArrayList(it) },
                onFail = { _videoListState.value = BaseState.Fail(it) }
            )
        }
    }
}