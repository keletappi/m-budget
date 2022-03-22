package com.example.mbudget.ui.budget

import androidx.lifecycle.*
import com.example.mbudget.model.Budget
import com.example.mbudget.model.Expense
import com.example.mbudget.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: BudgetRepository,
) : ViewModel() {

    val budgetId: UUID = UUID.fromString(savedStateHandle["budgetId"])

    val budget: LiveData<Budget> = repository.getBudget(budgetId).asLiveData().map { budget ->
        budget.copy(expenses = budget.expenses.sortedBy { expense -> expense.time })
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repository.addExpenseToBudget(expense, budgetId)
        }
    }

}