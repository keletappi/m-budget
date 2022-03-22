package com.example.mbudget.model

import java.math.BigDecimal

data class Budget(
    val name: String,
    val limit: BigDecimal,
    val currency: Currency,
    val expenses: List<Expense>,
) {
    val used: BigDecimal by lazy {
        expenses.sumOf { it.amountIn(currency).amount }
    }
}
