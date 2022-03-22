package com.example.mbudget.ui.budget

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mbudget.model.Budget
import com.example.mbudget.model.EUR
import com.example.mbudget.model.Expense
import com.example.mbudget.ui.BudgetHeader
import com.example.mbudget.ui.components.ExpenseListing
import com.example.mbudget.ui.components.PREVIEW_EXPENSES
import com.example.mbudget.ui.home.LoadingScreen
import com.example.mbudget.ui.home.toggle
import kotlinx.coroutines.launch

@Composable
fun BudgetScreen(
    budget: Budget?,
    saveExpense: (Expense) -> Unit,
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val sheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    var expenseSheetContentKey by remember { mutableStateOf(Any()) }

    BackHandler(enabled = sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

    val (editableExpense, setEditableExpense) = remember { mutableStateOf<Expense?>(null) }

    if (budget == null) {
        LoadingScreen()
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            ModalBottomSheetLayout(
                sheetState = sheetState,
                sheetContent = {
                    DefineExpenseSheetContent(
                        budget = budget,
                        baseExpense = editableExpense,
                        saveExpenseAction = {
                            coroutineScope.launch { sheetState.hide() }
                            saveExpense(it)
                            softwareKeyboardController?.hide()
                        },
                        contentKey = expenseSheetContentKey
                    )
                }) {
                Box(modifier = Modifier.fillMaxSize()) {
                    BudgetScreenContent(budget,
                        onExpenseClicked = {
                            coroutineScope.launch { sheetState.show() }
                            setEditableExpense(it)
                        }
                    )
                    FloatingActionButton(
                        modifier = Modifier
                            .padding(24.dp)
                            .align(Alignment.BottomEnd),
                        onClick = {
                            setEditableExpense(null)
                            expenseSheetContentKey = Any()
                            coroutineScope.launch { sheetState.toggle() }
                        }) {
                        // Set new key when sheet is opened to clear old content
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null
                        )
                    }

                }
            }
        }
    }
}

@Composable
private fun BudgetScreenContent(
    budget: Budget,
    onExpenseClicked: (Expense) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        BudgetHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            name = budget.name,
            used = budget.used,
            limit = budget.limit,
            currency = budget.currency,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(color = MaterialTheme.colors.primary)
        )

        ExpenseListing(
            expenses = budget.expenses,
            budgetCurrency = budget.currency,
            modifier = Modifier.weight(1.0f),
            onExpenseClicked = onExpenseClicked,
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
                expenses = PREVIEW_EXPENSES,
            ),
            saveExpense = {}
        )
    }
}

