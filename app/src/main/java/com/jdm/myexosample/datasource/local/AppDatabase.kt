package com.jdm.myexosample.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jdm.myexosample.entity.VideoEntity

@Database(
    entities = [VideoEntity::class], version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun videoDao(): VideoDao
}