package com.example.mbudget.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.lang.IllegalStateException
import java.time.Instant
import java.util.*

// TODO: Consider externalizing exchange rates.
// Having the rate in the expense means that moving an expense from one budget to another
// requires replacing the rate if the source and destination budgets do not share currency.
data class Expense(
    val name: String,
    val amount: Amount,
    val time: Instant = Instant.now(),
    val exchangeRate: ExchangeRate? = null,
    val expenseId: UUID = UUID.randomUUID(), // CAVEAT: No collision detection!
) {
    fun amountIn(currency: Currency): Amount {
        if (currency == amount.currency) return amount

        if (exchangeRate == null) {
            throw IllegalStateException("No exchange rate defined")
        }

        if (exchangeRate.to != currency) {
            throw IllegalStateException("Can't convert to $currency with exchange rate $exchangeRate")
        }

        return Amount(
            amount = amount.amount * exchangeRate.rate,
            currency = currency
        )
    }

}