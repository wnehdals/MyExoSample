package com.jdm.myexosample.repository

import com.jdm.myexosample.datasource.DataSource
import com.jdm.myexosample.datasource.local.LocalDataSource
import com.jdm.myexosample.datasource.remote.RemoteDataSource
import com.jdm.myexosample.entity.VideoEntity
import com.jdm.myexosample.response.FailResp
import com.jdm.myexosample.response.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {
    suspend fun getVideoList(onSuccess: (MutableList<Video>) -> Unit, onFail: (FailResp) -> Unit) {
        var result = remoteDataSource.getVideoList()!!
        if (result.isSuccessful) {
            onSuccess(result.body()!!.body.videoList)
        } else {
            onFail(FailResp(result.code(), result.message()))
        }
    }
    suspend fun insertVideoEntity(videoEntity: VideoEntity) {
        localDataSource.insertVideoEntity(videoEntity)
    }

    fun getVideoEntity(id: Int): Flow<VideoEntity> {
        return localDataSource.getVideoEntity(id)
            .onEach {
                if (it == null) { throw Exception() }
            }
            .catch { e ->
                emit(VideoEntity(id,"","","",0))
            }
    }
}