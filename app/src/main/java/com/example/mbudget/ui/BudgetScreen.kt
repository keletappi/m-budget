package com.example.mbudget.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mbudget.model.Budget
import com.example.mbudget.model.EUR
import com.example.mbudget.ui.util.ExpenseListing
import com.example.mbudget.ui.util.PREVIEW_EXPENSES

@Composable
fun BudgetScreen(
    budget: Budget,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Surface {
            BudgetHeader(
                modifier = Modifier.fillMaxWidth(),
                name = budget.name,
                used = budget.used,
                limit = budget.limit,
                currency = budget.currency,
            )
        }

        ExpenseListing(
            expenses = budget.expenses,
            budgetCurrency = budget.currency,
            modifier = Modifier.weight(1.0f)
        )
    }
}

@Composable
@Preview(widthDp = 320, heightDp = 500)
fun PreviewBudget() {
    MaterialTheme {
        BudgetScreen(
            budget = Budget(
                name = "Preview Budget",
                limit = 120.toBigDecimal(),
                currency = EUR,
                expenses = PREVIEW_EXPENSES
            ),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

