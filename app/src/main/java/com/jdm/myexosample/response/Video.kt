package com.jdm.myexosample.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jdm.myexosample.entity.VideoEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(
    @SerializedName("id")
    @Expose
    var id: Int = -1,

    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String = "",

    @SerializedName("video_url")
    @Expose
    var url: String = "",

    @SerializedName("title")
    @Expose
    var title: String = ""
) : Parcelable {
    fun toVideoEntity(position: Long): VideoEntity {
        return VideoEntity(id,thumbnail, url, title, position)
    }
    companion object {
        val EMPTY = Video()
    }
}
