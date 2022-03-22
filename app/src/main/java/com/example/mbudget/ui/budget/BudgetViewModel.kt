package com.example.mbudget.ui.budget

import androidx.lifecycle.*
import com.example.mbudget.model.Budget
import com.example.mbudget.model.Expense
import com.example.mbudget.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: BudgetRepository,
) : ViewModel() {

    val budgetId: UUID = UUID.fromString(savedStateHandle["budgetId"])

    @Suppress("MemberVisibilityCanBePrivate")
    val selectedYearMonth = MutableLiveData(YearMonth.now())

    val budget: LiveData<Budget> = selectedYearMonth.switchMap { selectedMonth ->
        // TODO: Show loading indicator while data is being refreshed
        repository.getBudget(budgetId, selectedMonth).asLiveData().map { budget ->
            budget.copy(expenses = budget.expenses.sortedBy { expense -> expense.time })
        }
    }

    fun saveExpense(expense: Expense) {
        viewModelScope.launch {
            repository.saveExpenseToBudget(expense, budgetId)
        }
    }

    fun nextMonth() {
        selectedYearMonth.value = selectedYearMonth.value?.plusMonths(1)?.coerceAtMost(YearMonth.now()) ?: YearMonth.now()
    }

    fun previousMonth() {
        selectedYearMonth.value = selectedYearMonth.value?.minusMonths(1) ?: YearMonth.now()
    }

}
