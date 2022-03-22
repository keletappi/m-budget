package com.example.mbudget.ui.budget

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.mbudget.ui.util.formattedAsDigit
import com.example.mbudget.ui.util.localTimeFormatted
import com.example.mbudget.ui.validation.isValidDigit
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import java.time.*

@Composable
fun DefineExpenseSheetContent(
    budget: Budget,
    saveExpenseAction: (Expense) -> Unit = {},
    contentKey: Any = Any(),
    baseExpense: Expense?,
) {
    val focusManager = LocalFocusManager.current
    val focusNextElement = {
        if (!focusManager.moveFocus(FocusDirection.Next)) {
            focusManager.moveFocus(FocusDirection.Down)
        }
    }

    val (name, setName) = remember(contentKey, baseExpense) {
        mutableStateOf(TextFieldValue(baseExpense?.name ?: ""))
    }

    val (amount, setAmount) = remember(contentKey, baseExpense) {
        mutableStateOf(TextFieldValue(baseExpense?.amount?.formattedAsDigit() ?: ""))
    }

    val (currency, setCurrency) = remember(contentKey, baseExpense) {
        mutableStateOf(baseExpense?.amount?.currency ?: budget.currency)
    }

    val (time, setTime) = remember(contentKey, baseExpense) {
        mutableStateOf(baseExpense?.time?.let {
            LocalDateTime.ofInstant(it, ZoneId.systemDefault())
        } ?: LocalDateTime.now())
    }

    val amountIsValid by derivedStateOf { amount.text.trim().isValidDigit() }

    val canSaveExpense = name.text.isNotEmpty() && amount.text.isNotEmpty() && amountIsValid

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

        Spacer(modifier = Modifier.height(8.dp))

        DateTimePicker(time, setTime)

        Spacer(modifier = Modifier.height(16.dp))

        SaveExpenseButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            saveExpenseAction = {
                val newName = name.text
                val newAmount = Amount(
                    amount.text.trim().replace(',', '.').toBigDecimal(),
                    currency
                )
                saveExpenseAction(
                    baseExpense
                        ?.copy(
                            name = newName,
                            amount = newAmount,
                            time = time.atZone(ZoneId.systemDefault()).toInstant()
                        )
                        ?: Expense(
                            name = newName,
                            amount = newAmount,
                            time = time.atZone(ZoneId.systemDefault()).toInstant()
                        )
                )
            },
            enabled = canSaveExpense
        )
    }
}

@Composable
private fun DateTimePicker(time: LocalDateTime, setTime: (LocalDateTime) -> Unit) {
    val activity = LocalContext.current as AppCompatActivity

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, color = MaterialTheme.colors.onSurface),
        onClick = {
            showDatePicker(
                activity = activity,
                date = time.toLocalDate(),
                setDate = { newDate ->
                    showTimePicker(
                        activity = activity,
                        time = time.toLocalTime(),
                        setTime = { newTime ->
                            setTime(
                                LocalDateTime.of(
                                    newDate.year,
                                    newDate.month,
                                    newDate.dayOfMonth,
                                    newTime.hour,
                                    newTime.minute,
                                )
                            )
                        }
                    )
                }
            )
        }
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = time.localTimeFormatted()
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
        placeholder = { Text(text = stringResource(R.string.create_expense_name_placeholder)) },
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
                modifier = Modifier
                    .weight(1f)
                    .alignByBaseline(),
                value = amount,
                placeholder = { Text(text = stringResource(R.string.create_expense_amount_placeholder)) },
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
private fun SaveExpenseButton(
    modifier: Modifier = Modifier,
    saveExpenseAction: () -> Unit,
    enabled: Boolean
) {
    TextButton(
        modifier = modifier,
        onClick = saveExpenseAction,
        enabled = enabled,
    ) {
        Text(stringResource(R.string.button_label_save_expense))
    }
}

fun showDatePicker(
    activity: AppCompatActivity,
    date: LocalDate,
    setDate: (LocalDate) -> Unit,
) {
    val picker = MaterialDatePicker.Builder.datePicker()
        .setSelection(date.atTime(12, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
        .build()

    picker.show(activity.supportFragmentManager, picker.toString())
    picker.addOnPositiveButtonClickListener {
        val localDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault());
        setDate(localDate.toLocalDate())
    }
}

fun showTimePicker(
    activity: AppCompatActivity,
    time: LocalTime,
    setTime: (LocalTime) -> Unit,
) {
    val picker = MaterialTimePicker.Builder()
        .setHour(time.hour)
        .setMinute(time.minute)
        .setInputMode(INPUT_MODE_CLOCK)
        .build()

    picker.show(activity.supportFragmentManager, picker.toString())
    picker.addOnPositiveButtonClickListener {
        setTime(LocalTime.of(picker.hour, picker.minute))
    }
}
