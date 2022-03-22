package com.example.mbudget.repository

import com.example.mbudget.db.BudgetDao
import com.example.mbudget.model.Budget
import com.example.mbudget.db.model.BudgetWithExpenses
import com.example.mbudget.db.model.DbBudget
import com.example.mbudget.db.model.DbExpense
import com.example.mbudget.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class BudgetRepository @Inject constructor(private val dao: BudgetDao) {

    fun getBudgets(): Flow<List<Budget>> = dao.observeBudgets().map { budgets ->
        budgets.map { budget ->
            budget.toLocalModel()
        }
    }

    fun getBudget(id: UUID): Flow<Budget> = dao.observeBudget(id).map { budget ->
        budget.toLocalModel()
    }

    suspend fun insertBudget(budget: Budget) {
        dao.insertOrUpdateBudget(
            DbBudget(
                budgetId = budget.budgetId,
                name = budget.name,
                limit = budget.limit,
                currency = budget.currency,
            )
        )
    }

    suspend fun saveExpenseToBudget(expense: Expense, budgetId: UUID) {
        dao.insertOrUpdateExpense(
            DbExpense(
                budgetId = budgetId,
                expenseId = expense.expenseId,
                name = expense.name,
                time = expense.time,
                exchangeRate = expense.exchangeRate,
                amount = expense.amount
            )
        )
    }

}

private fun BudgetWithExpenses.toLocalModel(): Budget {
    return Budget(
        budgetId = budget.budgetId,
        name = budget.name,
        currency = budget.currency,
        limit = budget.limit,
        expenses = expenses.map { it.toLocalModel() },
    )
}


private fun DbExpense.toLocalModel(): Expense {
    return Expense(
        name = name,
        amount = amount,
        time = time,
        exchangeRate = exchangeRate,
        expenseId = expenseId,
    )
}
