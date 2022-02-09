package com.jdm.myexosample.datasource

import com.jdm.myexosample.response.CommonResp
import retrofit2.Response

interface DataSource {
    suspend fun getVideoList(): Response<CommonResp>
}