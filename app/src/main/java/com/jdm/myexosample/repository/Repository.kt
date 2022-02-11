package com.jdm.myexosample.repository

import com.jdm.myexosample.datasource.DataSource
import com.jdm.myexosample.response.FailResp
import com.jdm.myexosample.response.Video
import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteDataSource: DataSource
) {
    suspend fun getVideoList(onSuccess: (MutableList<Video>) -> Unit, onFail: (FailResp) -> Unit) {
        var result = remoteDataSource.getVideoList()
        if (result.isSuccessful) {
            onSuccess(result.body()!!.body.videoList)
        } else {
            onFail(FailResp(result.code(), result.message()))
        }
    }
}