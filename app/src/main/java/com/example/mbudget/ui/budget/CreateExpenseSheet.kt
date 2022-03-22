package com.example.mbudget.ui.budget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.mbudget.R
import com.example.mbudget.model.Amount
import com.example.mbudget.model.Budget
import com.example.mbudget.model.Currency
import com.example.mbudget.model.Expense
import com.example.mbudget.ui.validation.isValidDigit

@Composable
fun CreateExpenseSheetContent(
    budget: Budget,
    createExpenseAction: (Expense) -> Unit = {},
    contentKey: Any = Any(),
) {
    val focusManager = LocalFocusManager.current
    val focusNextElement = {
        if (!focusManager.moveFocus(FocusDirection.Next)) {
            focusManager.moveFocus(FocusDirection.Down)
        }
    }

    val (name, setName) = remember(contentKey) { mutableStateOf(TextFieldValue("")) }
    val (amount, setAmount) = remember(contentKey) { mutableStateOf(TextFieldValue("")) }
    val (currency, setCurrency) = remember(contentKey) { mutableStateOf(budget.currency) }

    val amountIsValid by derivedStateOf { amount.text.trim().isValidDigit() }

    val canCreateExpense = name.text.isNotEmpty() && amount.text.isNotEmpty() && amountIsValid

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        NameInput(
            name = name,
            setName = setName,
            onNext = focusNextElement,
        )

        Spacer(modifier = Modifier.height(8.dp))

        AmountInput(
            amount = amount,
            setAmount = setAmount,
            currency = currency,
            hasError = amount.text.isNotEmpty() && !amountIsValid,
            onNext = focusNextElement,
        )

        Spacer(modifier = Modifier.height(16.dp))

        CreateExpenseButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            createExpense = {
                createExpenseAction(
                    Expense(
                        name = name.text,
                        amount = Amount(amount.text.trim().toBigDecimal(), currency),
                    )
                )
            },
            enabled = canCreateExpense
        )
    }
}

@Composable
private fun NameInput(
    name: TextFieldValue,
    setName: (TextFieldValue) -> Unit,
    onNext: () -> Unit,
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = name,
        placeholder = { Text(text = stringResource(R.string.expense_name_placeholder)) },
        onValueChange = setName,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
        ),
        keyboardActions = KeyboardActions(onNext = { onNext() }),
        maxLines = 1,
        singleLine = true,
    )
}

@Composable
fun AmountInput(
    amount: TextFieldValue,
    setAmount: (TextFieldValue) -> Unit,
    currency: Currency,
    hasError: Boolean,
    onNext: () -> Unit
) {
    Column {
        Row {
            TextField(
                modifier = Modifier.weight(1f).alignByBaseline(),
                value = amount,
                placeholder = { Text(text = stringResource(R.string.create_budget_limit_placeholder)) },
                onValueChange = { setAmount(it.copy(text = it.text.trim())) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(onNext = { onNext() }),
                isError = hasError,
                maxLines = 1,
                singleLine = true,
            )

            Spacer(modifier = Modifier.width(4.dp))
            Text(
                modifier = Modifier.alignByBaseline(),
                text = java.util.Currency.getInstance(currency.id).symbol,
            )
        }

        Text(
            text = stringResource(R.string.error_expense_amount_not_numeric),
            color = if (hasError) MaterialTheme.colors.onBackground else Color.Transparent
        )
    }
}

@Composable
private fun CreateExpenseButton(
    modifier: Modifier = Modifier,
    createExpense: () -> Unit,
    enabled: Boolean
) {
    TextButton(
        modifier = modifier,
        onClick = createExpense,
        enabled = enabled,
    ) {
        Text(stringResource(R.string.button_label_create_expense))
    }
}
