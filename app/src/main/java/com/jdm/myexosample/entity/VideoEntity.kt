package com.jdm.myexosample.entity

import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.annotation.concurrent.Immutable

@Immutable
@Entity(primaryKeys = [("id")])
data class VideoEntity(
    var id: Int,

    var thumbnail: String,

    var url: String,

    var title: String,

    var position: Long
)
