package com.github.rmitsubayashi.data.dao.typeConverter

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun toTimeStamp(date: Date): Long {
        return date.time
    }
}