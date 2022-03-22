package com.example.mbudget.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.mbudget.R
import com.example.mbudget.model.Budget
import com.example.mbudget.ui.BudgetHeader
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun HomeScreen(
    budgets: List<Budget>?,
    createBudget: (CreateBudgetInfo) -> Unit,
    openBudget: (UUID) -> Unit
) {

    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val sheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    var createBudgetSheetContentKey by remember { mutableStateOf(Any()) }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            CreateBudgetSheetContent(
                createBudgetAction = {
                    coroutineScope.launch { sheetState.hide() }
                    createBudget(it)
                    softwareKeyboardController?.hide()
                },
                contentKey = createBudgetSheetContentKey
            )
        }

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            HomeScreenContent(budgets, openBudget)
            FloatingActionButton(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomEnd),
                onClick = {
                    createBudgetSheetContentKey = Any()
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
        )

        Spacer(modifier = Modifier.height(16.dp))

        CreateBudgetButton(
            modifier = Modifier.align(CenterHorizontally),
            createBudget = { createBudgetAction(CreateBudgetInfo(name.text, limit.text.toBigDecimal())) },
            enabled = canCreateBudget,
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
        Text("Create budget")
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
        placeholder = { Text(text = stringResource(R.string.budget_name_placeholder)) },
        onValueChange = setName,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        keyboardActions = KeyboardActions(onNext = { onNext() }),
        maxLines = 1,
    )
}

@Composable
private fun LimitInput(
    limit: TextFieldValue,
    setLimit: (TextFieldValue) -> Unit,
    limitHasError: Boolean,
    onNext: () -> Unit,
) {
    Column {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = limit,
            placeholder = { Text(text = stringResource(R.string.budget_limit_placeholder)) },
            onValueChange = setLimit,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions(onNext = { onNext() }),
            isError = limitHasError
        )

        Text(
            text = stringResource(R.string.error_budget_limit_not_numeric),
            color = if (limitHasError) MaterialTheme.colors.onBackground else Color.Transparent
        )
    }
}

@Composable
private fun HomeScreenContent(
    budgets: List<Budget>?,
    openBudget: (UUID) -> Unit,
) {
    when {
        budgets == null -> LoadingScreen()
        budgets.isEmpty() -> NoBudgetsPrompt()
        else -> BudgetListing(budgets, openBudget)
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
    }
}

@Composable
private fun BudgetListing(
    budgets: List<Budget>,
    openBudget: (UUID) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(budgets) { budget ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(2.dp, MaterialTheme.colors.primary),
                onClick = { openBudget(budget.budgetId) },
                elevation = 4.dp,
            ) {
                BudgetHeader(budget)
            }
        }

    }
}

@Composable
private fun NoBudgetsPrompt() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Center
    ) {
        Text(
            text = stringResource(R.string.no_budgets_prompt),
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center
        )
    }
}

suspend fun ModalBottomSheetState.toggle() = if (isVisible) hide() else show()

