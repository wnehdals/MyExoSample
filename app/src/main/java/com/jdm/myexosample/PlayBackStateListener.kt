package com.jdm.myexosample

import android.util.Log
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.upstream.HttpDataSource

import com.google.android.exoplayer2.upstream.DataSpec

import com.google.android.exoplayer2.upstream.HttpDataSource.HttpDataSourceException
import com.google.android.exoplayer2.upstream.HttpDataSource.InvalidResponseCodeException


class PlayBackStateListener: Player.Listener {
    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        when (playbackState) {
            Player.STATE_IDLE -> {
                Log.e(TAG2, "idle")
            }
            Player.STATE_BUFFERING -> {
                Log.e(TAG2, "buffering")
            }
            Player.STATE_READY -> {
                Log.e(TAG2, "ready")
            }
            Player.STATE_ENDED -> {
                Log.e(TAG2, "end")
            }
        }
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)
        Log.e(TAG3, "playWhenReady : ${playWhenReady} / reason : ${reason}")
    }

    override fun onPlayerError(error: PlaybackException) {
        val cause: Throwable? = error.cause
        if (cause is HttpDataSourceException) {

        }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        Log.e(TAG4, "mediaId : ${mediaItem?.mediaId} / ${mediaItem?.mediaMetadata}")
    }

    companion object {
        val TAG1 = "PlayBackStateListener"
        val TAG2 = "onPlayBackStateChanged"
        val TAG3 = "onPlayWhenReadyChanged"
        val TAG4 = "onMediaItemTransition"
    }
}