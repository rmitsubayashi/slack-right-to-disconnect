package com.github.rmitsubayashi.data.local.sqlite.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.rmitsubayashi.domain.model.RecipientType
import java.util.*

@Entity
data class Thread (
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "date")val date: Date,
    @PrimaryKey val threadID: String,
    @ColumnInfo(name = "type") val parentType: RecipientType,
    @ColumnInfo(name = "parent_slack_name") val parentSlackName: String,
    @ColumnInfo(name = "parent_display_name") val parentDisplayName: String
)