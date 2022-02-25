package com.jdm.myexosample.datasource

import com.jdm.myexosample.entity.VideoEntity
import com.jdm.myexosample.response.CommonResp
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface DataSource {
    suspend fun getVideoList(): Response<CommonResp>?
    fun getVideoEntity(id: Int): Flow<VideoEntity>
    suspend fun insertVideoEntity(videoEntity: VideoEntity)
}