package com.example.mbudget.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mbudget.model.Currency
import java.math.BigDecimal
import java.util.*

@Entity
data class DbBudget(
    @PrimaryKey val budgetId: UUID = UUID.randomUUID(),
    val name: String,
    val limit: BigDecimal,
    val currency: Currency,
)