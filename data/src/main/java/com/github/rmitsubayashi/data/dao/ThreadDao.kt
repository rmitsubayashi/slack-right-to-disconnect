package com.github.rmitsubayashi.data.dao

import androidx.room.*
import com.github.rmitsubayashi.data.model.Thread

@Dao
interface ThreadDao {
    @Query("SELECT * FROM thread")
    fun getAll(): List<Thread>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg thread: Thread)

    @Query("DELETE FROM thread")
    fun delete()
}