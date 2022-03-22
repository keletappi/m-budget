package com.example.mbudget.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant

class ExpenseTest {

    @Test
    fun amountInSameCurrency() {
        val currency = Currency("EUR", "€")

        val original = Expense(
            name = "Foobar",
            amount = Amount(10.toBigDecimal(), currency),
            time = Instant.now()
        )


        val result = original.amountIn(currency)
        assertThat(result.amount).isEqualToIgnoringScale(original.amount.amount)
        assertThat(result.currency).isEqualTo(original.amount.currency)

    }

    @Test
    fun amountInDifferentCurrency() {

        val original = Expense(
            name = "Foobar",
            amount = Amount(50.toBigDecimal(), EUR),
            time = Instant.now(),
            exchangeRate = ExchangeRate(
                rate = 10.toBigDecimal(),
                from = EUR,
                to = SEK,
            )
        )

        val result = original.amountIn(SEK)
        assertThat(result.amount).isEqualToIgnoringScale(500)
        assertThat(result.currency).isEqualTo(SEK)

    }

}