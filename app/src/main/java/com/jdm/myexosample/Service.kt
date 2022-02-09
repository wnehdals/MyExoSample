package com.jdm.myexosample

import com.jdm.myexosample.response.CommonResp
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("sample/video")
    suspend fun getVideoList() : Response<CommonResp>

}