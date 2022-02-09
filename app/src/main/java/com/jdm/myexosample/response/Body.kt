package com.jdm.myexosample.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("video_list")
    @Expose
    var videoList: MutableList<Video> = mutableListOf<Video>()
    )
