package com.example.mbudget.db

import androidx.room.*
import com.example.mbudget.db.model.BudgetWithExpenses
import com.example.mbudget.db.model.DbBudget
import com.example.mbudget.db.model.DbExpense
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface BudgetDao {

    @Query("SELECT * FROM DbBudget")
    suspend fun getBudgets(): List<BudgetWithExpenses>

    @Query("SELECT * FROM DbBudget")
    fun observeBudgets(): Flow<List<BudgetWithExpenses>>

    @Query("SELECT * FROM DbBudget WHERE budgetId = :budgetId")
    fun observeBudget(budgetId: UUID): Flow<BudgetWithExpenses>

    @Query("SELECT * FROM DbExpense where budgetId = :budgetId")
    fun observeExpenses(budgetId: UUID): Flow<List<DbExpense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateBudget(budget: DbBudget)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateExpense(expense: DbExpense)

    @Delete
    suspend fun deleteExpense(expense: DbExpense)
}
