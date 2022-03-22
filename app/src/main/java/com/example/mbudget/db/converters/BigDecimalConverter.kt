package com.example.mbudget.db.converters

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalConverter {
    @TypeConverter
    fun bigDecimalToString(input: BigDecimal?): String {
        return input?.toPlainString() ?: ""
    }

    @TypeConverter
    fun stringToBigDecimal(input: String?): BigDecimal {
        if (input.isNullOrBlank()) return BigDecimal.ZERO
        return input.toBigDecimalOrNull() ?: BigDecimal.ZERO
    }
}