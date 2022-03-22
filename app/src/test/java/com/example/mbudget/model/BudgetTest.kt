package com.example.mbudget.model

import com.google.common.truth.Truth.*

import org.junit.Test
import java.time.Instant

class BudgetTest {

    @Test
    fun totalNoExpenses() {
        val budget = Budget(
            name = "Test",
            limit = 100.toBigDecimal(),
            currency = EUR,
            expenses = emptyList()
        )

        assertThat(budget.used).isEqualToIgnoringScale(0)
    }

    @Test
    fun totalSameCurrency() {
        val budget = Budget(
            name = "Test",
            limit = 100.toBigDecimal(),
            currency = EUR,
            expenses = listOf(
                Expense(
                    name = "Foo",
                    amount = Amount("2.50".toBigDecimal(), EUR),
                    time = Instant.now()
                ),
                Expense(
                    name = "Bar",
                    amount = Amount("7.50".toBigDecimal(), EUR),
                    time = Instant.now()
                ),
            )
        )

        assertThat(budget.used).isEqualToIgnoringScale(10)
    }

    @Test
    fun totalMixedCurrency() {
        val budget = Budget(
            name = "Test",
            limit = 100.toBigDecimal(),
            currency = EUR,
            expenses = listOf(
                Expense(
                    name = "Foo",
                    amount = Amount("2.50".toBigDecimal(), EUR),
                    time = Instant.now()
                ),
                Expense(
                    name = "Bar",
                    amount = Amount("75.00".toBigDecimal(), SEK),
                    time = Instant.now(),
                    exchangeRate = ExchangeRate("0.1".toBigDecimal(), from = SEK, to = EUR)
                ),
            )
        )

        assertThat(budget.used).isEqualToIgnoringScale(10)
    }
}