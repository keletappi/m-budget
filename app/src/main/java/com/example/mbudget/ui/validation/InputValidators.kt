package com.example.mbudget.ui.validation

fun String.isValidDigit() = try {
    this.replace(',', '.').toBigDecimal()
    true
} catch (e: NumberFormatException) {
    false
}
