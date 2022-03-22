package com.example.mbudget.ui.util

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mbudget.model.Amount
import com.example.mbudget.model.Currency
import com.example.mbudget.model.EUR
import com.example.mbudget.model.Expense
import com.example.mbudget.ui.ExpenseRow
import java.time.Instant


@Composable
fun ExpenseListing(
    expenses: List<Expense>,
    budgetCurrency: Currency,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(expenses) { expense ->
            ExpenseRow(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                expense = expense,
                budgetCurrency = budgetCurrency
            )
        }
    }
}

@Composable
@Preview(widthDp = 320, heightDp = 500)
fun PreviewExpenseListing() {
    MaterialTheme {
        ExpenseListing(
            modifier = Modifier.fillMaxSize(),
            expenses = PREVIEW_EXPENSES,
            budgetCurrency = EUR,
        )
    }
}

val PREVIEW_EXPENSES = listOf(
    Expense("Foobar 1", Amount("12.34", EUR), Instant.parse("2022-03-20T15:03:43Z")),
    Expense("Foobar 2", Amount("12.34", EUR), Instant.parse("2022-03-20T15:03:43Z")),
    Expense("Foobar 3", Amount("12.34", EUR), Instant.parse("2022-03-20T15:03:43Z")),
    Expense("Foobar 4", Amount("12.34", EUR), Instant.parse("2022-03-20T15:03:43Z")),
    Expense("Foobar 5", Amount("12.34", EUR), Instant.parse("2022-03-20T15:03:43Z")),
    Expense("Foobar 6", Amount("12.34", EUR), Instant.parse("2022-03-20T15:03:43Z")),
    Expense("Foobar 7", Amount("12.34", EUR), Instant.parse("2022-03-20T15:03:43Z")),
    Expense("Foobar 8", Amount("12.34", EUR), Instant.parse("2022-03-20T15:03:43Z")),
    Expense("Foobar 9", Amount("12.34", EUR), Instant.parse("2022-03-20T15:03:43Z")),
    Expense("Foobar 10", Amount("12.34", EUR), Instant.parse("2022-03-20T15:03:43Z")),
    Expense("Foobar 11", Amount("12.34", EUR), Instant.parse("2022-03-20T15:03:43Z")),
    Expense("Foobar 12", Amount("12.34", EUR), Instant.parse("2022-03-20T15:03:43Z")),
    Expense("Foobar 13", Amount("12.34", EUR), Instant.parse("2022-03-20T15:03:43Z")),
)