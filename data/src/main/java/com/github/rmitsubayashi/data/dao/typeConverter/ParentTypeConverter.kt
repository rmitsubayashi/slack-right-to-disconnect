package com.github.rmitsubayashi.data.dao.typeConverter

import androidx.room.TypeConverter
import com.github.rmitsubayashi.domain.model.RecipientType

class ParentTypeConverter {
    @TypeConverter
    fun fromID(id: Int): RecipientType {
        return RecipientType.valueOf(id)
    }

    @TypeConverter
    fun toRecipientType(type: RecipientType): Int {
        return type.id
    }
}