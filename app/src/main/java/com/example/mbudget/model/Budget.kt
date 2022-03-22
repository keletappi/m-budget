package com.example.mbudget.model

import java.math.BigDecimal
import java.util.*

data class Budget(
    val name: String,
    val limit: BigDecimal,
    val currency: Currency,
    val expenses: List<Expense>,
    val budgetId: UUID = UUID.randomUUID(),
) {
    val used: BigDecimal by lazy {
        expenses.sumOf { it.amountIn(currency).amount }
    }
}
