package com.example.mbudget.db.model

import androidx.room.Embedded
import androidx.room.Relation

data class BudgetWithExpenses(
    @Embedded val budget: DbBudget,
    @Relation(parentColumn = "budgetId", entityColumn = "budgetId") val expenses: List<DbExpense>
)