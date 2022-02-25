package com.jdm.myexosample.datasource.remote

import com.jdm.myexosample.datasource.DataSource
import com.jdm.myexosample.entity.VideoEntity
import com.jdm.myexosample.response.CommonResp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val service: Service): DataSource {
    override suspend fun getVideoList(): Response<CommonResp> {
        return service.getVideoList()
    }

    override fun getVideoEntity(id: Int): Flow<VideoEntity> = flow {
        emit(VideoEntity(0,"","","",0))
    }

    override suspend fun insertVideoEntity(videoEntity: VideoEntity) {
    }
}