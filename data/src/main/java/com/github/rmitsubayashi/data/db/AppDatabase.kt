package com.github.rmitsubayashi.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.rmitsubayashi.data.dao.BookmarkDao
import com.github.rmitsubayashi.data.model.Bookmark

@Database(entities = [Bookmark::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        const val DB_NAME = "r2dc_db"
    }
}