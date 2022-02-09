package com.jdm.myexosample

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel  @Inject constructor(): ViewModel() {
    var coroutineException: CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->

    }
}