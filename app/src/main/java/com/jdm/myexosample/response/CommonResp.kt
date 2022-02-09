package com.jdm.myexosample.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CommonResp(
    @SerializedName("statusCode")
    @Expose
    var statusCode:Int = 404,

    @SerializedName("body")
    @Expose
    var body:Body = Body()
    )
