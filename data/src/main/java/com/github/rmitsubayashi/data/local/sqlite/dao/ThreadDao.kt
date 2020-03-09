package com.github.rmitsubayashi.data.local.sqlite.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.rmitsubayashi.data.local.sqlite.model.Thread

@Dao
interface ThreadDao {
    @Query("SELECT * FROM thread")
    fun getAll(): List<Thread>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg thread: Thread)

    @Query("DELETE FROM thread")
    fun delete()
}