package com.jdm.myexosample.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("id")
    @Expose
    var id:Int = -1,

    @SerializedName("thumbnail")
    @Expose
    var thumbnail:String = "",

    @SerializedName("video_url")
    @Expose
    var url:String = ""
    )
