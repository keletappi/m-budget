package com.example.mbudget.ui.home

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.mbudget.R
import com.example.mbudget.model.Currency
import com.example.mbudget.model.EUR

@Composable
@Preview
fun CreateBudgetSheetContent(
    createBudgetAction: (CreateBudgetInfo) -> Unit = {},
    contentKey: Any = Any()
) {
    val focusManager = LocalFocusManager.current
    val focusNextElement = {
        if (!focusManager.moveFocus(FocusDirection.Next)) {
            focusManager.moveFocus(FocusDirection.Down)
        }
    }

    val (name, setName) = remember(contentKey) { mutableStateOf(TextFieldValue("")) }
    val (limit, setLimit) = remember(contentKey) { mutableStateOf(TextFieldValue("")) }

    // FIXME: This now supports whole numbers only. Accept single decimal separator as well
    val limitHasError by derivedStateOf { !limit.text.isDigitsOnly() }

    val canCreateBudget = name.text.isNotEmpty() && limit.text.isNotEmpty() && !limitHasError

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

        LimitInput(
            limit = limit,
            setLimit = setLimit,
            limitHasError = limitHasError,
            onNext = focusNextElement,
            currency = EUR,
        )

        Spacer(modifier = Modifier.height(16.dp))

        CreateBudgetButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            createBudget = {
                createBudgetAction(
                    CreateBudgetInfo(
                        name.text,
                        limit.text.toBigDecimal(),
                    )
                )
            },
            enabled = canCreateBudget,
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
        placeholder = { Text(text = stringResource(R.string.create_budget_name_placeholder)) },
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
private fun LimitInput(
    limit: TextFieldValue,
    setLimit: (TextFieldValue) -> Unit,
    currency: Currency,
    limitHasError: Boolean,
    onNext: () -> Unit,
) {
    Column {
        Row {

            TextField(
                modifier = Modifier.weight(1f).alignByBaseline(),
                value = limit,
                placeholder = { Text(text = stringResource(R.string.create_budget_limit_placeholder)) },
                onValueChange = { setLimit(it.copy(text = it.text.trim())) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(onNext = { onNext() }),
                isError = limitHasError,
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
            text = stringResource(R.string.error_budget_limit_not_numeric),
            color = if (limitHasError) MaterialTheme.colors.onBackground else Color.Transparent
        )
    }
}

@Composable
private fun CreateBudgetButton(
    modifier: Modifier = Modifier,
    createBudget: () -> Unit,
    enabled: Boolean
) {
    TextButton(
        modifier = modifier,
        onClick = createBudget,
        enabled = enabled,
    ) {
        Text(stringResource(R.string.button_label_create_budget))
    }
}
