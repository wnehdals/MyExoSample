package com.jdm.myexosample.di

import com.jdm.myexosample.datasource.local.LocalDataSource
import com.jdm.myexosample.datasource.local.VideoDao
import com.jdm.myexosample.datasource.remote.RemoteDataSource
import com.jdm.myexosample.datasource.remote.Service
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideRemoteDataSource(service: Service): RemoteDataSource {
        return RemoteDataSource(service)
    }
    @Provides
    @Singleton
    fun provideLocalDataSource(videoDao: VideoDao, @IoDispatcher ioDispatcher: CoroutineDispatcher): LocalDataSource {
        return LocalDataSource(videoDao, ioDispatcher)
    }
}