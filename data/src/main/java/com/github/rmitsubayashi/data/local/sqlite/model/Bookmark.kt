package com.github.rmitsubayashi.data.local.sqlite.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.rmitsubayashi.domain.model.RecipientType

@Entity
data class Bookmark (
    @PrimaryKey val slackID: String,
    @ColumnInfo(name = "slack_name") val slackName: String,
    @ColumnInfo(name = "display_name") val displayName: String,
    @ColumnInfo(name = "type") val type: RecipientType
)