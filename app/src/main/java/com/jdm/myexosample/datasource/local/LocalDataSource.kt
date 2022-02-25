package com.jdm.myexosample.datasource.local

import com.jdm.myexosample.datasource.DataSource
import com.jdm.myexosample.datasource.remote.Service
import com.jdm.myexosample.di.IoDispatcher
import com.jdm.myexosample.entity.VideoEntity
import com.jdm.myexosample.response.CommonResp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class LocalDataSource@Inject constructor(private val videoDao: VideoDao, private val ioDispatcher: CoroutineDispatcher): DataSource{
    override suspend fun getVideoList(): Response<CommonResp>? {
        return null
    }

    override fun getVideoEntity(id: Int): Flow<VideoEntity> {
        return videoDao.getVideoEntity(id).flowOn(ioDispatcher)
    }
    override suspend fun insertVideoEntity(videoEntity: VideoEntity) {
        videoDao.insertVideoEntity(videoEntity)
    }
}