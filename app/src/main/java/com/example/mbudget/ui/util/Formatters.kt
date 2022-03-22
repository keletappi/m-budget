package com.example.mbudget.ui.util

import com.example.mbudget.model.Amount
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.Temporal
import java.util.*

fun Amount.formatted(): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.currency = Currency.getInstance(currency.id)
    format.minimumFractionDigits = 2
    format.maximumFractionDigits = 2
    return format.format(amount)
}

fun Number.formattedAs(currency: com.example.mbudget.model.Currency): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.currency = Currency.getInstance(currency.id)
    format.minimumFractionDigits = 2
    format.maximumFractionDigits = 2
    return format.format(this)
}

fun Temporal.localTimeFormatted(): String {
    return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
        .format(this)
}

