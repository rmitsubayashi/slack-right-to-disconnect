package com.github.rmitsubayashi.data.dao

import androidx.room.*
import com.github.rmitsubayashi.data.model.Bookmark

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmark")
    fun getAll(): List<Bookmark>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg bookmarks: Bookmark)

    @Query("SELECT COUNT(1) FROM bookmark WHERE slackID = :id")
    fun bookmarkExists(id: String): Int

    @Delete
    fun delete(bookmark: Bookmark)
}