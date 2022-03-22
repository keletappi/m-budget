package com.example.mbudget.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mbudget.model.*
import com.example.mbudget.ui.components.ExpenseRowSemantics.TestTag.AMOUNT_BUDGET_CURRENCY
import com.example.mbudget.ui.components.ExpenseRowSemantics.TestTag.AMOUNT_ORIGINAL_CURRENCY
import com.example.mbudget.ui.components.ExpenseRowSemantics.TestTag.NAME
import com.example.mbudget.ui.components.ExpenseRowSemantics.TestTag.TIME
import com.example.mbudget.ui.util.formattedAsCurrency
import com.example.mbudget.ui.util.localTimeFormatted
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun ExpenseRow(
    expense: Expense,
    budgetCurrency: Currency,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                modifier = Modifier.semantics { testTag = NAME },
                text = expense.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                modifier = Modifier.semantics { testTag = TIME },
                text = LocalDateTime.ofInstant(expense.time, ZoneId.systemDefault())
                    .localTimeFormatted()
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                modifier = Modifier.semantics { testTag = AMOUNT_BUDGET_CURRENCY },
                text = expense.amountIn(budgetCurrency).formattedAsCurrency()
            )
            if (expense.amount.currency != budgetCurrency) {
                Text(
                    modifier = Modifier.semantics { testTag = AMOUNT_ORIGINAL_CURRENCY },
                    text = expense.amount.formattedAsCurrency(),
                    style = MaterialTheme.typography.body1.copy(fontSize = 10.sp)
                )
            }
        }
    }
}

/**
 * Semantics for testing [ExpenseRow].
 */
object ExpenseRowSemantics {
    object TestTag {
        const val NAME = "ExpenseName"
        const val TIME = "Timestamp"
        const val AMOUNT_BUDGET_CURRENCY = "AmountInBudgetCurrency"
        const val AMOUNT_ORIGINAL_CURRENCY = "AmountInOriginalCurrency"
    }
}

@Composable
@Preview(
    name = "Expense, same currency",
    widthDp = 320
)
private fun PreviewExpenseSameCurrency() {
    MaterialTheme {
        ExpenseRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.surface),
            expense = Expense(
                "Sample expense name",
                amount = Amount("12.34".toBigDecimal(), EUR),
                time = Instant.parse("2022-03-20T15:03:43Z")
            ),
            budgetCurrency = EUR,
        )
    }

}

@Composable
@Preview(
    name = "Expense, different currency",
    widthDp = 320
)
private fun PreviewExpenseDifferentCurrency() {
    MaterialTheme {
        ExpenseRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.surface),
            expense = Expense(
                "Sample expense name for something created in foreign currency",
                amount = Amount("12.34".toBigDecimal(), SEK),
                time = Instant.parse("2022-03-20T15:03:43Z"),
                exchangeRate = ExchangeRate(10.toBigDecimal(), SEK, EUR)
            ),
            budgetCurrency = EUR,
        )
    }

}