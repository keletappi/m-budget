package com.example.mbudget.db.converters

import androidx.room.TypeConverter
import com.example.mbudget.model.Currency

class CurrencyConverter {
    @TypeConverter
    fun toCurrency(currency: String?): Currency? = currency?.let { Currency(it) }

    @TypeConverter
    fun fromCurrency(currency: Currency?): String? = currency?.id

}