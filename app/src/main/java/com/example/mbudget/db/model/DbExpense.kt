package com.example.mbudget.db.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.mbudget.model.Amount
import com.example.mbudget.model.ExchangeRate
import java.time.Instant
import java.util.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = DbBudget::class,
            parentColumns = ["budgetId"],
            childColumns = ["budgetId"]
        )
    ]
)
data class DbExpense(
    @PrimaryKey val expenseId: UUID = UUID.randomUUID(), // CAVEAT: No collision detection!
    val name: String,
    val time: Instant = Instant.now(),
    @Embedded val amount: Amount,
    @Embedded val exchangeRate: ExchangeRate? = null,
    val budgetId: UUID,
)