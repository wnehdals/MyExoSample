package com.jdm.myexosample.repository

import com.jdm.myexosample.datasource.DataSource
import com.jdm.myexosample.response.Video
import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteDataSource: DataSource
) {
    suspend fun getVideoList(): MutableList<Video> {
        var result = remoteDataSource.getVideoList()
        return result.body()!!.body.videoList
    }
}