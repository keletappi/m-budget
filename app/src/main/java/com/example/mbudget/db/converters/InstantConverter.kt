package com.example.mbudget.db.converters

import androidx.room.TypeConverter
import java.time.Instant

class InstantConverter {
    @TypeConverter
    fun toDate(epochMillis: Long?): Instant? = epochMillis?.let { Instant.ofEpochMilli(it) }

    @TypeConverter
    fun fromDate(instant: Instant?): Long? = instant?.toEpochMilli()

}