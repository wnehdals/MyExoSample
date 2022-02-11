package com.jdm.myexosample.state

import com.jdm.myexosample.response.FailResp

sealed class PlayState {
    object Uninitialized: PlayState()

    object Loading: PlayState()

    object Ready: PlayState()

    object Playing: PlayState()

    object Pause: PlayState()

    object Finish: PlayState()
}
