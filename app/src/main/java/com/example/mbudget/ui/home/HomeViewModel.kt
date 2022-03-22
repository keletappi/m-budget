package com.example.mbudget.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mbudget.model.Budget
import com.example.mbudget.model.EUR
import com.example.mbudget.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

data class CreateBudgetInfo(
    val name: String,
    val limit: BigDecimal
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: BudgetRepository
) : ViewModel() {

    val budgets: LiveData<List<Budget>> = repository.getBudgetsForCurrentMonth().asLiveData()

    fun createBudget(createBudget: CreateBudgetInfo) {
        viewModelScope.launch {
            repository.insertBudget(
                Budget(
                    createBudget.name,
                    createBudget.limit,
                    EUR,
                    emptyList()
                )
            )
        }
    }

}
