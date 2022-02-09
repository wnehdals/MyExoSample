package com.jdm.myexosample.di

import com.jdm.myexosample.datasource.DataSource
import com.jdm.myexosample.datasource.RemoteDataSource
import com.jdm.myexosample.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideRepository(remoteDataSource: RemoteDataSource): Repository {
        return Repository(remoteDataSource)
    }
}