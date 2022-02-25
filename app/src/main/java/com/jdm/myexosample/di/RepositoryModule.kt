package com.jdm.myexosample.di

import com.jdm.myexosample.datasource.local.LocalDataSource
import com.jdm.myexosample.datasource.remote.RemoteDataSource
import com.jdm.myexosample.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideRepository(remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource): Repository {
        return Repository(remoteDataSource, localDataSource)
    }
}