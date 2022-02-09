package com.jdm.myexosample

import androidx.lifecycle.viewModelScope
import com.jdm.myexosample.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository): BaseViewModel() {
    fun getVideoList() {
        viewModelScope.launch(coroutineException) {
            repository.getVideoList()
        }

    }
}