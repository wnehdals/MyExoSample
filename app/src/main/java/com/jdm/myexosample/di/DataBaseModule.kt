package com.jdm.myexosample.di

import android.content.Context
import androidx.room.Room
import com.jdm.myexosample.datasource.local.AppDatabase
import com.jdm.myexosample.datasource.local.VideoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideRoomDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "MyExoPlayer.db")
            .allowMainThreadQueries()
            .build()
    }
    @Provides
    @Singleton
    fun provideVideoDao(appDatabase: AppDatabase): VideoDao {
        return appDatabase.videoDao()
    }
}
