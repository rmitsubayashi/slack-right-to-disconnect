package com.github.rmitsubayashi.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.rmitsubayashi.data.dao.BookmarkDao
import com.github.rmitsubayashi.data.dao.ThreadDao
import com.github.rmitsubayashi.data.dao.typeConverter.DateConverter
import com.github.rmitsubayashi.data.dao.typeConverter.ParentTypeConverter
import com.github.rmitsubayashi.data.model.Bookmark
import com.github.rmitsubayashi.data.model.Thread

@Database(entities = [Bookmark::class, Thread::class], version = 1)
@TypeConverters(DateConverter::class, ParentTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun threadDao(): ThreadDao

    companion object {
        const val DB_NAME = "r2dc_db"
    }
}