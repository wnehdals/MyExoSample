package com.jdm.myexosample.di

import com.jdm.myexosample.datasource.RemoteDataSource
import com.jdm.myexosample.Service
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    fun provideRemoteDataSource(service: Service): RemoteDataSource {
        return RemoteDataSource(service)
    }
}