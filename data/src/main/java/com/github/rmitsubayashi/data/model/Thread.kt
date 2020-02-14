package com.github.rmitsubayashi.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.rmitsubayashi.domain.model.RecipientType
import java.util.Date

@Entity
data class Thread (
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "date")val date: Date,
    @PrimaryKey val threadID: String,
    @ColumnInfo(name = "type") val parentType: RecipientType
)