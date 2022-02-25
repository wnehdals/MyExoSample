package com.jdm.myexosample.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jdm.myexosample.entity.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoEntity(videoEntity: VideoEntity)


    @Query("SELECT * FROM VIDEOENTITY WHERE id = :id_" )
    fun getVideoEntity(id_: Int): Flow<VideoEntity>
}