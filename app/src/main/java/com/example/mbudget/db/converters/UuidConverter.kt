package com.example.mbudget.db.converters

import androidx.room.TypeConverter
import java.util.*

class UuidConverter {
    @TypeConverter
    fun toUuid(uuid: String?): UUID? = uuid?.let { UUID.fromString(it) }

    @TypeConverter
    fun fromUuid(uuid: UUID?): String? = uuid?.toString()
}