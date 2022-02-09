package com.jdm.myexosample

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenRequest(
    @SerializedName("drm_type")
    @Expose
    var drm_type:String = "Widevine",

    @SerializedName("site_id")
    @Expose
    var site_id: String = "LXQD",

    @SerializedName("user_id")
    @Expose
    var user_id:String = "pan8899",

    @SerializedName("cid")
    @Expose
    var cid: String = "grow-dash",

    @SerializedName("policy")
    @Expose
    var policy: String = "",

    @SerializedName("timestamp")
    @Expose
    var timestamp: String = "",

    @SerializedName("response_format")
    @Expose
    var response_format: String =  "original",

    @SerializedName("key_rotation")
    @Expose
    var key_rotation: Boolean = false
)
