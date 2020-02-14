package com.github.rmitsubayashi.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.rmitsubayashi.domain.model.RecipientType

@Entity
data class Bookmark (
    @PrimaryKey val slackID: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "type") val type: RecipientType
)