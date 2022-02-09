package com.jdm.myexosample.datasource

import com.jdm.myexosample.Service
import com.jdm.myexosample.response.CommonResp
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val service: Service): DataSource {
    override suspend fun getVideoList(): Response<CommonResp> {
        return service.getVideoList()
    }
}