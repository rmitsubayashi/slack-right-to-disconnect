package com.github.rmitsubayashi.data.local.sqlite

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.rmitsubayashi.data.local.sqlite.dao.BookmarkDao
import com.github.rmitsubayashi.data.local.sqlite.dao.ThreadDao
import com.github.rmitsubayashi.data.local.sqlite.dao.typeConverter.DateConverter
import com.github.rmitsubayashi.data.local.sqlite.dao.typeConverter.ParentTypeConverter
import com.github.rmitsubayashi.data.local.sqlite.model.Bookmark
import com.github.rmitsubayashi.data.local.sqlite.model.Thread

@Database(entities = [Bookmark::class, Thread::class], version = 1)
@TypeConverters(DateConverter::class, ParentTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun threadDao(): ThreadDao

    companion object {
        const val DB_NAME = "r2dc_db"
    }
}